package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.*;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class TestDataGenerator implements ApplicationRunner {

    private Logger LOGGER = LoggerFactory.getLogger(TestDataGenerator.class);

    private FakeData faker;
    private IEventService eventService;
    private IHolidayService holidayService;
    private ITrainerService trainerService;
    private ICustomerService customerService;
    private ITagService tagService;
    // ENVIRONMENT SETUP DEFAULT
    private int NO_TRAINERS = 10;
    private int NO_COURSES = 50;
    private int SIMULATED_DAYS = 100;
    private int NO_RENTS = 10;
    private int NO_BIRTHDAYS = 10;
    private int NO_CONSULTATION = 20;



    @Autowired
    public TestDataGenerator(FakeData fakeData, IEventService eventService, IHolidayService holidayService, ITrainerService trainerService, ICustomerService customerService, ITagService tagService) {
        this.faker = fakeData;
        this.eventService = eventService;
        this.holidayService = holidayService;
        this.trainerService = trainerService;
        this.customerService = customerService;
        this.tagService = tagService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // check command line argument
        if (args.containsOption("testData")) {
            if (args.getOptionValues("testData").get(0).equals("yes")) {
                LOGGER.debug("Spring-Boot Started With Option For Initial Test Data Set To True! Start Simulation With Random Data");

                if (args.containsOption("trainers")) {
                    NO_TRAINERS = Integer.parseInt(args.getOptionValues("trainers").get(0));
                }
                if (args.containsOption("courses")) {
                    NO_COURSES = Integer.parseInt(args.getOptionValues("courses").get(0));
                }
                if (args.containsOption("days")) {
                    SIMULATED_DAYS = Integer.parseInt(args.getOptionValues("days").get(0));
                }
                if (args.containsOption("rents")) {
                    NO_RENTS = Integer.parseInt(args.getOptionValues("rents").get(0));
                }
                if (args.containsOption("bdays")) {
                    NO_BIRTHDAYS = Integer.parseInt(args.getOptionValues("bdays").get(0));
                }
                if (args.containsOption("consultations")) {
                    NO_CONSULTATION = Integer.parseInt(args.getOptionValues("consultations").get(0));
                }

                startSimulation();

                System.out.println(
                    "##################################\n" +
                    "#  Test Data Creation Completed  #\n" +
                    "##################################\n"
                );

                return;
            }
        }

        LOGGER.debug("Spring Boot Started Without Test Data Initialization");
    }

    private void startSimulation() throws Exception {
        List<Trainer> trainers = new LinkedList();
        String password = "e9a75486736a550af4fea861e2378305c4a555a05094dee1dca2f68afea49cc3a50e8de6ea131ea521311f4d6fb054a146e8282f8e35ff2e6368c1a62e909716";

        // create initial trainer set
        for (int i = 0; i < NO_TRAINERS; i++) {
            Trainer trainer = faker.fakeNewTrainerEntity();
            trainer.setPassword(password);
            Trainer saved = trainerService.save(trainer);

            trainers.add(saved);
        }

        for (Tag tag: faker.getFakedTags()) {
            tagService.save(tag);
        }

        for (int i = 0; i < NO_COURSES; i++) {
            Event course = faker.fakeNewCourseEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(course);
            } catch(ValidationException | ServiceException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_RENTS; i++) {
            Event rent = faker.fakeNewRent(SIMULATED_DAYS);

            try {
                eventService.save(rent);
            } catch(ValidationException | ServiceException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_BIRTHDAYS; i++) {
            Event birthday = faker.fakeNewBirthdayEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(birthday);
            } catch(ValidationException | ServiceException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_CONSULTATION; i++) {
            Event consultation = faker.fakeConsultationEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(consultation);
            } catch(ValidationException | ServiceException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }
    }


    public void fillDatabase(int count){
        for(int i = 0; i< 10; i++){
            try {
                Trainer trainer = faker.fakeNewTrainerEntity();
                trainerService.save(trainer);
            }catch(ServiceException | ValidationException e){
                //failure is fine
            }
        }
        List<Trainer> trainers = new LinkedList<>();
        try {
            trainers = trainerService.getAll();
        }catch(ServiceException e){
            //
        }
        for(int i = count/5; i>0; i--){
            try {
                Event event = faker.fakeNewCourseEntity(trainers, 2);
                System.out.println(event.toString2());
                eventService.save(event);
            }catch(ValidationException | ServiceException e){
                //
            }
        }
        List<Event> events = new LinkedList<>();
        try {
            events = eventService.getAllEvents();
        }catch(ServiceException e){
            //
        }
        for(int n = count; n > 0; n--){
                Customer customer = faker.fakeNewCustomerEntity();
                boolean found = false;
                for(Event e: events
                ) {
                    if(faker.randomInt(1,100) < 50){
                        customer.setId(null);
                    }else{
                        customer.setId((long)(faker.randomInt(1,100)));
                    }
                    if(customer.getId() == null) {
                        if(e.getMinAge() != null && e.getMinAge() != null) {
                            if(customer.getBirthOfChild().isAfter(
                                LocalDateTime.now().minusYears(
                                    e.getMaxAge())) &&
                               customer.getBirthOfChild().isBefore(
                                   LocalDateTime.now().minusYears(
                                       e.getMinAge()))) {

                                e.setCustomers(new LinkedHashSet<>());
                                e.getCustomers().add(customer);
                                try {
                                    eventService.updateCustomers(e);
                                    found = true;
                                }
                                catch(ValidationException | NotFoundException | ServiceException exc) {

                                }
                            }
                        }
                    }
                }
                if(found = false){
                    n++;
                }
        }
    }
}
