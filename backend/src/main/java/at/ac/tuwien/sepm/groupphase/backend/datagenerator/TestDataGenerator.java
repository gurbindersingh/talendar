package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class TestDataGenerator implements ApplicationRunner {

    private Logger LOGGER = LoggerFactory.getLogger(TestDataGenerator.class);

    private  FakeData faker;
    private IEventService eventService;
    private IHolidayService holidayService;
    private ITrainerService trainerService;

    // ENVIRONMENT SETUP DEFAULT
    private int NO_TRAINERS = 10;
    private int NO_COURSES = 50;
    private int SIMULATED_DAYS = 100;
    private int NO_RENTS = 10;
    private int NO_BIRTHDAYS = 10;
    private int NO_CONSULTATION = 20;



    @Autowired
    public TestDataGenerator(FakeData fakeData, IEventService eventService, IHolidayService holidayService, ITrainerService trainerService) {
        this.faker = fakeData;
        this.eventService = eventService;
        this.holidayService = holidayService;
        this.trainerService = trainerService;
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
                return;
            }
        }

        LOGGER.debug("Spring Boot Started Without Test Data Initialization");
    }

    private void startSimulation() throws Exception {
        List<Trainer> trainers = new LinkedList();

        // create initial trainer set
        for (int i = 0; i < NO_TRAINERS; i++) {
            Trainer trainer = faker.fakeNewTrainerEntity();
            Trainer saved = trainerService.save(trainer);

            trainers.add(saved);
        }

        for (int i = 0; i < NO_COURSES; i++) {
            Event course = faker.fakeNewCourseEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(course);
            } catch(ValidationException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_RENTS; i++) {
            Event rent = faker.fakeNewRent(SIMULATED_DAYS);

            try {
                eventService.save(rent);
            } catch(ValidationException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_BIRTHDAYS; i++) {
            Event birthday = faker.fakeNewBirthdayEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(birthday);
            } catch(ValidationException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }

        for (int i = 0; i < NO_CONSULTATION; i++) {
            Event consultation = faker.fakeConsultationEntity(trainers, SIMULATED_DAYS);

            try {
                eventService.save(consultation);
            } catch(ValidationException e) {
                // its a simulation, some add courses will fail, but that is okay
            }
        }
    }
}
