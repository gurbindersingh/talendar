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
                    if(!this.trainerRepository.existsById(event.getTrainer().getId())){
                        InvalidEntityException e = new InvalidEntityException("Trainer mit Id nicht gefunden");
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
                    for(Customer c: event.getCustomers()
                    ) {
                        sendCancelationMail(c.getEmail(), event, c);
                    }
                    LOGGER.info("Sending information mail to admin");
                    infoMail.sendAdminEventInfoMail(event, "Neue Raummiete", "newEvent");
                    return event;

                }
                catch(InvalidEntityException e) {
                    throw new ValidationException(e.getMessage(), e);
                }catch(EmailException e){
                    throw new ValidationException("Something went wrong while attempting to send an email: " + e.getMessage(), e);
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
                    for(Customer c: event.getCustomers()
                    ) {
                        sendCancelationMail(c.getEmail(), event, c);
                    }
                    LOGGER.info("Sending information mail to admin");
                    infoMail.sendAdminEventInfoMail(event, "Neuer Beratungstermin", "newEvent");
                    return event;
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException(e.getMessage(), e);
                }catch(EmailException e){
                    throw new ValidationException("Something went wrong while attempting to send an email: " + e.getMessage(), e);
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

        Optional<Event> result;

        try {
            result = eventRepository.findById(id);

        } catch(DataAccessException dae){
            throw new ServiceException("Error while performing a data access operation", dae);
        }

        if(result.isPresent() && !result.get().isDeleted()){  //if event exists and not deleted the return
            return result.get();
        } else {
            throw new NotFoundException("The event with id " + id + " does not exist");
        }
    }


    @Override
    public List<Event> getAllEvents (Long trainerId) throws ValidationException, ServiceException {
        return null;
    }

    @Transactional
    @Override
    public Event updateCustomers(Event event) throws ValidationException, NotFoundException,
                                                     ServiceException {
        if(event.getCustomers() != null) {
            LocalDateTime timeOfUpdate = LocalDateTime.now();
            Optional<Event> queryResult;
            Event currentEvent;

            int sizeOfNewEventList = event.getCustomers().size();

            try {
                queryResult = this.eventRepository.findById(event.getId());
                if(queryResult.isPresent()) {



                    currentEvent = queryResult.get();

                    int sizeOfPersistedList = currentEvent.getCustomers().size();

                    Integer lastEmailId = null;

                    Set<Customer> newCustomers = new HashSet<>();
                    Customer newCustomer = null;
                    for(Customer x : event.getCustomers()) {
                        x.setId(null);                      //id must be null
                        this.validator.validateCustomer(x);
                        if(x.getEvents() != null) {         // TODO: Maybe not needed.
                            x.getEvents()
                             .add(
                                 event);    //no duplicate, so add event of old customers will be ignored
                        } else {
                            Set<Event> events = new HashSet<>();
                            events.add(event);
                            x.setEvents(events);
                        }

                        if(x.getEmailId() != null){             //setup emailId for new Customer...
                            lastEmailId = x.getEmailId();
                        } else {

                            //Customer to insert

                            if(lastEmailId == null){
                                lastEmailId = 1;
                            } else {
                                lastEmailId++;
                            }
                            x.setEmailId(lastEmailId);
                            LOGGER.info("x.getEmailId: " + x.getEmailId());
                        }

                        newCustomers.add(x);
                        newCustomer = x;
                    }



                    event.setCustomers(newCustomers);

                    event.setUpdated(timeOfUpdate);




                    Event persistedEvent = mergeEvent(currentEvent, event);



                    eventRepository.flush();

                    LOGGER.info(sizeOfNewEventList + " und " + sizeOfPersistedList);

                    if(sizeOfPersistedList < sizeOfNewEventList){
                        // A SIGN IN IS HAPPENING
                        //DO EMAIL WITH CUSTOMER ID:::::::



                        for(Customer x : persistedEvent.getCustomers()) {   // Get newest customer again because we need the id

                            newCustomer = x;

                            LOGGER.info("EMAIL ID: " + x.getEmailId());
                        }





                        LOGGER.info(persistedEvent.toString());

                        LOGGER.info("Prepare Email for sign off");
                        sendCancelationMail(newCustomer.getEmail(), persistedEvent,
                                         newCustomer
                        );   //create a sign off email and send it to customer
                        LOGGER.info("Email sent");
                    }


                    ////////////////////////////////////////////////////////

                    return persistedEvent;


                } else {
                    LOGGER.error("Event with id " + event.getId() + " not found");
                    throw new NotFoundException("");
                }
            }
            catch(DataAccessException dae) {
                LOGGER.error("Error: " + dae);
                throw new ServiceException("", dae);
            }
            catch(InvalidEntityException ve) {
                throw new ValidationException(ve.getMessage(), ve);
            }
            catch(Exception m) {
                throw new ServiceException(m);
            }
        }
        LOGGER.error("No Customer to add");
        throw new ServiceException("", null);
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
            url = "http://localhost:4200/cancelEvent?id=" + event.getId() + "&emailId=" + customer.getEmailId();
        } else {
            url = "http://localhost:4200/cancelEvent?id=" + event.getId();
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
                "\n Falls Sie diesen Event stornieren wollen, clicken Sie bitte einfach auf diesen link: \n";
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
