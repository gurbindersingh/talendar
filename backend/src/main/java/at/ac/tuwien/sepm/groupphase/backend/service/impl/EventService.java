package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TimeNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TrainerNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.HolidayRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.RoomUseRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.CancelationException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.apache.tomcat.jni.Local;
import org.aspectj.weaver.ast.Not;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.rowset.serial.SerialException;
import javax.validation.Valid;
import javax.validation.Validation;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EventService implements IEventService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RoomUseRepository roomUseRepository;
    private final Validator validator;
    private final TrainerRepository trainerRepository;
    private final HolidayRepository holidayRepository;
    private final InfoMail infoMail;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    public EventService (EventRepository eventRepository, Validator validator,
                         RoomUseRepository roomUseRepository, TrainerRepository trainerRepository,
                         HolidayRepository holidayRepository, InfoMail infoMail
    ) {
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.roomUseRepository = roomUseRepository;
        this.trainerRepository = trainerRepository;
        this.holidayRepository = holidayRepository;
        this.infoMail = infoMail;
    }


    @Override
    public Event save(Event event) throws ValidationException, ServiceException {
        LOGGER.info("Prepare to save new Event");
        LocalDateTime now = LocalDateTime.now();
        event.setCreated(now);
        event.setUpdated(now);
        event.setDeleted(false);

        /*
         * We have to set the foreign key property of the 'many-side' roomUses explicitly
         * RoomUses will be automatically inserted event without this statement
         * but obviously (or not) jpa is not able to determine the fk key on his own
         */
        if(event.getRoomUses() != null) {
            for(RoomUse roomUse : event.getRoomUses()) {
                roomUse.setEvent(event);
            }
        }

        try {
            Thread.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        switch(event.getEventType()) {
            case Birthday:
                  try {
                      validator.validateEvent(event);


                      event = synchRoomUses(event);
                      event = synchCustomers(event);
                      event.setTrainer(findTrainerForBirthday(event.getRoomUses(), event.getBirthdayType()));
                      validator.validateTrainer(event.getTrainer());
                      System.out.println(event);


                      try {
                          isAvailable(event.getRoomUses());
                      }
                      catch(TimeNotAvailableException e) {
                          throw new ValidationException(
                              e.getMessage(),
                              e
                          );
                      }

                      event = eventRepository.save(event);
                      eventRepository.flush();
                      System.out.println(event.getId());
                      for(Customer c: event.getCustomers()
                      ) {
                          sendCancelationMail(c.getEmail(), event, c);
                      }
                      LOGGER.info("Sending information mail to admin");
                      infoMail.sendAdminEventInfoMail(event, "Neuer Geburtstag", "newEvent");
                      return event;
                  }
                  catch(InvalidEntityException e) {
                      throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e);
                  }catch(TrainerNotAvailableException e){
                      throw new ServiceException("There are no Trainers available for this birthday " + e.getMessage(),e);
                  }catch(EmailException e){
                      throw new ValidationException("Something went wrong while attempting to send an email: " + e.getMessage(), e);
                  }
            case Course:
                try {
                    validator.validateEvent(event);
                    if(!this.trainerRepository.existsById(event.getTrainer().getId())) {
                        InvalidEntityException e =
                            new InvalidEntityException("Trainer mit Id nicht gefunden");
                        throw new ValidationException(e.getMessage(), e);
                    }
                    try {
                        isAvailable(event.getRoomUses());
                    }
                    catch(TimeNotAvailableException e) {
                        throw new ValidationException(
                            e.getMessage(),
                            e
                        );
                    }
                    try{
                        LOGGER.info("Sending information mail to admin");
                        infoMail.sendAdminEventInfoMail(event, "Neuer Kurs", "newEvent");
                    }catch(EmailException e){

                    }
                    return eventRepository.save(event);
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException(e.getMessage(), e);
                }
            case Rent:
                try {
                    setAutoGeneratedName(event);
                    validator.validateEvent(event);

                    try {
                        isAvailable(event.getRoomUses());
                    }
                    catch(TimeNotAvailableException e) {
                        throw new ValidationException(
                            e.getMessage(),
                            e
                        );
                    }
                    event = eventRepository.save(event);
                    eventRepository.flush();
                    System.out.println(event.getId());
                    for(Customer c : event.getCustomers()
                    ) {
                        sendCancelationMail(c.getEmail(), event, c);
                    }
                    LOGGER.info("Sending information mail to admin");
                    infoMail.sendAdminEventInfoMail(event, "Neue Raummiete", "newEvent");
                    return event;
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException(e.getMessage(), e);
                }
                catch(EmailException e) {
                    throw new ValidationException(
                        "Something went wrong while attempting to send an email: " + e.getMessage(),
                        e
                    );
                }
            case Consultation:
                try {
                    validator.validateEvent(event);
                    trainerAvailable(event.getTrainer(), event.getRoomUses());
                    try {
                        isAvailable(event.getRoomUses());
                    }
                    catch(TimeNotAvailableException e) {
                        throw new ValidationException(
                            e.getMessage(),
                            e
                        );
                    }
                    event = eventRepository.save(event);
                    eventRepository.flush();
                    System.out.println(event.getId());
                    for(Customer c : event.getCustomers()
                    ) {
                        sendCancelationMail(c.getEmail(), event, c);
                    }
                    LOGGER.info("Sending information mail to admin");
                    infoMail.sendAdminEventInfoMail(event, "Neuer Beratungstermin", "newEvent");
                    return event;
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException(e.getMessage(), e);
                }
                catch(EmailException e) {
                    throw new ValidationException(
                        "Something went wrong while attempting to send an email: " + e.getMessage(),
                        e
                    );
                }
                catch(TrainerNotAvailableException e) {
                    throw new ValidationException(
                        "The specified trainer is not available during the allocated time frame" +
                        e.getMessage(),
                        e
                    );
                }
        }

        return event;
    }


    @Override
    public Event getEventById(Long id) throws NotFoundException, ServiceException {
        LOGGER.info("Try to retrieve event with id " + id);

        Event result;

        try {
            result = eventRepository.findByIdAndDeletedFalse(id);
        }
        catch(DataAccessException dae) {
            throw new ServiceException("Error while performing a data access operation", dae);
        }

        if(result != null){
            LOGGER.info("Event with id found: " + result);
            return result;
        } else {
            throw new NotFoundException("The event with id " + id + " does not exist");
        }
    }


    @Override
    public List<Event> getAllEvents(Long trainerId) throws ValidationException, ServiceException {
        return null;
    }


    @Transactional
    @Override
    public Event updateCustomers(Event event) throws ValidationException, NotFoundException,
                                                     ServiceException {
        LOGGER.info("Event to update customers: " + event);
        if(event == null) {
            LOGGER.error("Event is null");
            throw new ServiceException("", null);
        }
        if(event.getCustomers() == null ||
           event.getCustomers().size() != 1) {  //only one customer can be added or removed
            LOGGER.error("No Customer to add or customer list size is not one");
            throw new ServiceException("", null);
        }

        Optional<Event> queryResult;
        Event persistedEvent;
        Customer customerToAddOrRemove = null;


        //get time of now
        LocalDateTime now = LocalDateTime.now();

        //sleep 1 millisecond so update is in past(constraint in event)
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        }catch(InterruptedException e){
            throw new ServiceException("Internal Server error", e);
        }


        // fetch customer to add or remove
        for(Customer x : event.getCustomers()) {
            customerToAddOrRemove = x;
        }

        // fetch persisted event
        try {
            queryResult = this.eventRepository.findById(event.getId());
            if(queryResult.isPresent()) {
                persistedEvent = queryResult.get();
            } else {
                LOGGER.error("Event with id " + event.getId() + " not found");
                throw new NotFoundException("");
            }
        }
        catch(DataAccessException dae) {
            LOGGER.error("Error: " + dae);
            throw new ServiceException("", dae);
        }

        if(customerToAddOrRemove == null){
            LOGGER.error("Customer is null");
            throw new ServiceException("", null);
        }

        event.setUpdated(now);


        if(customerToAddOrRemove.getId() == null) {
            // here is happening a sign in
            LOGGER.info("A sign in is happening");

            // validate new customer
            try {
                this.validator.validateCustomerForCourseSign(customerToAddOrRemove, event.getMinAge(), event.getMaxAge(), event.getEndOfApplication());
            }
            catch(InvalidEntityException ve) {
                LOGGER.error("Invalid Customer to add");
                throw new ValidationException(ve.getMessage(), ve);
            }


            Set<Customer> customerListWithNewCustomer = new HashSet<>();
            Integer greatestEmailId = null;

            // prepare list of customers
            for(Customer x : persistedEvent.getCustomers()) {
                if(greatestEmailId == null) {
                    greatestEmailId = x.getEmailId();
                }
                if(x.getEmailId() > greatestEmailId.intValue()){
                    greatestEmailId = x.getEmailId();
                }
                customerListWithNewCustomer.add(x);
            }

            // prepare email id for new customer
            if(greatestEmailId == null) {
                // first customer to add
                greatestEmailId = 1;
            } else {
                greatestEmailId++;
            }
            customerToAddOrRemove.setEmailId(greatestEmailId);


            // TODO Events
            Set<Event> events = new HashSet<>();
            events.add(event);
            customerToAddOrRemove.setEvents(events);

            if(customerListWithNewCustomer.add(customerToAddOrRemove)) { //if new customer to add dont exist in persisted customer list then update
                event.setCustomers(customerListWithNewCustomer);

                mergeEvent(persistedEvent, event);
                this.eventRepository.flush();

                // send a sign off email to customer
                try {
                    LOGGER.info("Prepare Email for sign off");
                    sendCancelationMail(customerToAddOrRemove.getEmail(), event,
                                        customerToAddOrRemove
                    );   //create a sign off email and send it to customer
                    LOGGER.info("Email sent");
                }
                catch(EmailException e) {
                    LOGGER.error("Email error: " + e.getMessage());
                    throw new ServiceException("", null);
                }
            }
        } else {
            // here is happening a sign off
            LOGGER.info("Sign off is happening");
            if(customerToAddOrRemove.getEmailId() == null) {
                LOGGER.error("Customer to remove has no email id");
                throw new ServiceException("", null);
            }

            Set<Customer> customerSet = new HashSet<>();
            boolean customerToRemoveFound = false;

            for(Customer x : persistedEvent.getCustomers()){
                if(!(x.getEmailId().intValue() == customerToAddOrRemove.getEmailId().intValue())){
                    customerSet.add(x);
                } else{
                    customerToRemoveFound = true;
                }
            }

            if(!customerToRemoveFound){
                LOGGER.error("Customer with email id " + customerToAddOrRemove.getEmailId() + " not found");
                throw new ServiceException("", null);
            }

            event.setCustomers(customerSet);

            mergeEvent(persistedEvent, event);
            this.eventRepository.flush();
        }


        // fetch new persisted event for return
        try {
            queryResult = this.eventRepository.findById(event.getId());
            if(queryResult.isPresent()) {
                return queryResult.get();
            } else {
                LOGGER.error("Event with id " + event.getId() + " not found");
                throw new NotFoundException("");
            }
        }
        catch(DataAccessException dae) {
            LOGGER.error("Error: " + dae);
            throw new ServiceException("", dae);
        }
    }



    @Override
    public List<Event> getAllEvents() throws ServiceException {
        LOGGER.info("Try to retrieve list of all events");

        try {
            return this.eventRepository.findAll();
        }
        catch(DataAccessException e) {
            throw new ServiceException(
                "Error while performing a data access operation to retrieve all events", e);
        }
    }


    @Transactional
    @Override
    public Event update(Event event) throws ValidationException, NotFoundException, ServiceException{
        LocalDateTime timeOfUpdate = LocalDateTime.now();

        Optional<Event> queryResult;
        Event currentEvent;

        switch(event.getEventType()) {
            case Course:
                try {
                   this.validator.validateCourseForUpdate(event);
                } catch(InvalidEntityException ve) {
                   throw new ValidationException(ve.getMessage(), ve);
                }

                try {

                    Event eventFromDb = this.eventRepository.findByIdAndDeletedFalse(event.getId());
                    if(eventFromDb == null){
                        LOGGER.error("Event with id " + event.getId() + " not found, maybe deleted");
                        throw new NotFoundException("");
                    }
                    if(eventFromDb.getCustomers().size() > event.getMaxParticipants()){
                        throw new ValidationException("Es sind schon mehr angemeldet als Ihrer Eingabe bei maximale Teilnehmerzahl", null);
                    }



                    if(event.getCustomers() != null){
                        Set<Customer> customers = new HashSet<>();
                        for(Customer x : event.getCustomers()){
                            x.setId(null);
                            customers.add(x);
                        }
                        event.setCustomers(customers);
                    }

                   queryResult = this.eventRepository.findById(event.getId());
                   if(queryResult.isPresent()){
                       LOGGER.debug("Course with id found");
                       currentEvent = queryResult.get();
                       event.setUpdated(timeOfUpdate);
                       return mergeEvent(currentEvent, event);
                   } else{
                       LOGGER.error("Event with id " + event.getId() + " not found");
                       throw new NotFoundException("");
                   }

                } catch(DataAccessException dae){
                    LOGGER.error("Error: " + dae);
                    throw new ServiceException("", dae);
                }
        }

        throw new ServiceException("", null);
    }


    private Event mergeEvent(Event persisted, Event newVersion) {
        LOGGER.info("Event will be merged now");
        persisted.setName(newVersion.getName());
        persisted.setCustomers(newVersion.getCustomers());

        //COURSE
        persisted.setDescription(newVersion.getDescription());
        persisted.setMaxParticipants(newVersion.getMaxParticipants());
        persisted.setMinAge(newVersion.getMinAge());
        persisted.setMaxAge(newVersion.getMaxAge());
        persisted.setPrice(newVersion.getPrice());
        persisted.setUpdated(newVersion.getUpdated());
        return persisted;
    }


    @Override
    public List<Event> getAllFutureCourses(){
        return eventRepository.findByEventTypeEqualsAndDeletedFalse(EventType.Course);
    }


    private void setAutoGeneratedName (Event event) {
        if(event.getCustomers() == null || event.getRoomUses() == null) {
        }
        else if(event.getEventType() == EventType.Rent) {
            for(Customer customer : event.getCustomers()) {
                for(RoomUse roomUse : event.getRoomUses()) {
                    event.setName(EventType.Rent +
                                  "-" +
                                  customer.getFirstName() +
                                  " " +
                                  customer.getLastName() +
                                  "-" +
                                  roomUse.getBegin());
                }
            }
        }
    }


    @Override
    public void deleteEvent(Long id) {
        Event event = eventRepository.getOne(id);
        eventRepository.deleteThisEvent(id);
        if(event!=null){
            try {
                infoMail.sendAdminEventInfoMail(event, "Event storniert", "deleteEvent");
            }catch(EmailException e){
                LOGGER.error("Unable to send InfoMail to admin about deleted event");
            }
        }
    }


    @Override
    public void cancelEvent(Long id) throws ValidationException {
        try{
            Event event = eventRepository.getOne(id);
            if(event.getEventType() != EventType.Course) {
                validator.validateCancelation(event);
            }else{
                eventRepository.deleteThisEvent(id);
            }
        }catch(CancelationException e){
            throw new ValidationException("The cancelation was ordered too late: " +  e.getMessage(), e);
        }
        eventRepository.deleteThisEvent(id);


    }


    public Trainer findTrainerForBirthday(List<RoomUse> roomUses, String birthdayType) throws TrainerNotAvailableException{
        List<Trainer> appropriateTrainers = trainerRepository.findByBirthdayTypes(birthdayType);;
        Collections.shuffle(appropriateTrainers);
        for(Trainer t : appropriateTrainers) {
            try {
                trainerAvailable(t, roomUses);
                return t;
            }
            catch(TrainerNotAvailableException e) {
                throw new TrainerNotAvailableException("There are no trainers who can do a " +
                                                       birthdayType +
                                                       " birthday during the allotted time");
            }
        }
        return null;
    }


    public Event synchRoomUses (Event event) {
        for(RoomUse x : event.getRoomUses()) {
            x.setEvent(event);
        }
        return event;
    }


    public Event synchCustomers(Event event){
        Set<Event> events = new HashSet<>();
        events.add(event);
        for(Customer x: event.getCustomers()){
            x.setEvents(events);
        }
        return event;
    }


    public void trainerAvailable(Trainer trainer, List<RoomUse> roomUses)throws TrainerNotAvailableException{
        List<RoomUse> trainersEvents = roomUseRepository.findByEvent_Trainer_IdAndBeginGreaterThanEqualAndEvent_DeletedFalse(trainer.getId(), LocalDateTime.now());
        List<Holiday> trainerHoliday = holidayRepository.findByTrainer_Id(trainer.getId());

        for(RoomUse x : roomUses) {
            for(RoomUse db : trainersEvents) {
                if(x.getBegin().isAfter(db.getBegin()) &&
                   x.getBegin().isBefore(db.getEnd()) ||
                   x.getEnd().isBefore(db.getEnd()) &&
                   x.getEnd().isAfter(db.getBegin()) ||
                   x.getBegin().isBefore(db.getBegin()) &&
                   x.getEnd().isAfter(db.getEnd()) ||
                   x.getEnd().isEqual(db.getEnd()) ||
                   x.getBegin().isEqual(db.getBegin())) {
                    throw new TrainerNotAvailableException(
                        "The specified trainer is not available for the allocated time frame");
                }
            }
            for(Holiday db : trainerHoliday) {
                if(x.getBegin().isAfter(db.getHolidayStart()) &&
                   x.getBegin().isBefore(db.getHolidayEnd()) ||
                   x.getEnd().isBefore(db.getHolidayEnd()) &&
                   x.getEnd().isAfter(db.getHolidayStart()) ||
                   x.getBegin().isBefore(db.getHolidayStart()) &&
                   x.getEnd().isAfter(db.getHolidayEnd()) ||
                   x.getEnd().isEqual(db.getHolidayEnd()) ||
                   x.getBegin().isEqual(db.getHolidayStart())) {
                    throw new TrainerNotAvailableException(
                        "The specified trainer is not available for the allocated time frame");
                }
            }
        }
    }


    public void isAvailable (List<RoomUse> roomUseList) throws TimeNotAvailableException {
        LOGGER.info("Check if Roomuses are available");
        LocalDateTime now = LocalDateTime.now();
        List<RoomUse> dbRooms = roomUseRepository.findByBeginGreaterThanEqualAndDeletedFalse(now);
        for(RoomUse x : roomUseList) {
            for(RoomUse db : dbRooms) {
                LOGGER.info("Database row begin: " + db.getBegin() +  " vs to insert begin: " + x.getBegin());
                if(x.getRoom() == db.getRoom()) {
                    if(x.getBegin().isAfter(db.getBegin()) &&
                       x.getBegin().isBefore(db.getEnd()) ||
                       x.getEnd().isBefore(db.getEnd()) &&
                       x.getEnd().isAfter(db.getBegin()) ||
                       x.getBegin().isBefore(db.getBegin()) &&
                       x.getEnd().isAfter(db.getEnd()) ||
                       x.getEnd().isEqual(db.getEnd()) && x.getBegin().isEqual(db.getBegin()) ||
                       x.getEnd().isEqual(db.getEnd()) ||
                       x.getBegin().isEqual(db.getBegin())) {
                        throw new TimeNotAvailableException(
                            "Von " + x.getBegin() + " bis " + x.getEnd() + " ist der Raum " + x.getRoom() + " belegt");
                    }
                }


            }
        }
        LOGGER.info("All roomuses are available");
    }


    public void sendCancelationMail(String email, Event event, Customer customer) throws EmailException {
            String to = email;
            String from = "testingsepmstuffqse25@gmail.com";
            String password = "This!is!a!password!";
            String host = "smtp.gmail.com";
            Properties props = System.getProperties();
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.pwd", password);
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);

            try{
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setSubject("Stornierungslink fur den Event am " + event.getRoomUses().get(0).getBegin().format(formatter));
                mimeMessage.setText(createCancelationMessage(event, customer));
                Transport transport = session.getTransport("smtp");
                transport.connect(host, 587, from, password);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();


            }catch(MessagingException e){
                throw new EmailException(" " + e.getMessage());
            }
    }


    private String createCancelationMessage(Event event, Customer customer) throws MessagingException {

        String url;
        if(event.getEventType() == EventType.Course){
            url = "http://localhost:4200/event/cancel?id=" + event.getId() + "&emailId=" + customer.getEmailId();
        } else {
            url = "http://localhost:4200/event/cancel?id=" + event.getId();
        }
        URL urll = null;
        try {
            urll = new URL(url);
        }
        catch(MalformedURLException e) {
            throw new MessagingException("Malformed Url exception: " + e.getMessage(), e);
        }

        String msg = "";

        msg += "Hallo " + customer.getFirstName() + " " + customer.getLastName() + "!";
        msg += "\n\n Danke, dass Sie sich bei uns für " +
               translateEnumWithArtikel(event.getEventType()) +
               " angemeldet haben.";
        if(event.getEventType() == EventType.Course){
            msg += "\n Ende der Abmeldefrist: ";
            msg += event.getEndOfApplication().format(formatter) + "\n";
            msg += "\n Falls Sie sich abmelden wollen, klicken Sie auf diesen Link: \n";
        } else {
            msg +=
                "\n Falls Sie diesen Event stornieren wollen, klicken Sie bitte einfach auf diesen link: \n";
        }
        msg += urll;

        return msg;
    }


    public String translateEnumWithArtikel(EventType eventType){
        switch (eventType){
            case Birthday:
                return "ein Geburtstag";
            case Consultation:
                return "einen Beratungstermin";
            case Rent:
                return "eine Mietung";
            case Course:
                return "einen Kurs";
        }
        return "";
    }
}
