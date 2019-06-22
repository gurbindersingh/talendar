package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.*;
import java.util.*;

@Component
public class FakeData {
    private Faker faker;
    private Random random = new Random();

    private final String[] birthdayTypes = { "Rocket", "DryIce", "Superhero", "Photo", "Painting" };

    // count
    private static int consultationCount = 0;


    public FakeData() {
        faker = new Faker(new Locale("de-DE"));
    }


    /**
     * ######################
     * # FAKE ATOMIC VALUES #
     * ######################
     */


    public int randomInt(int min, int max) {
        return (int) ( Math.random() * ( ( max - min ) + 1 ) ) + min;
    }


    private String fakeEmail() {
        return faker.internet().emailAddress();
    }


    private String fakePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }


    private String fakeFirstName() {
        return faker.name().firstName();
    }


    private String fakeLastName() {
        return faker.name().lastName();
    }


    private Long fakeID() {
        return (long) faker.number().numberBetween(0, 1000000);
    }


    private int fakeMinuteOfTime_15Steps() {
        int gamble = random.nextInt(4);

        switch(gamble) {
            case 0:
                return 0;

            case 1:
                return 15;

            case 2:
                return 30;

            case 3:
                return 45;

            default:
                return 0;
        }
    }


    /**
     * #####################
     * # Fake Context Data #
     * #####################
     */


    private LocalDateTime fakePastTimeAfter2000() {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.valueOf("2000-01-01"),
                                                     Date.valueOf("2019-05-14")
                                            )
                                            .toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime fakeChildBirthday() {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.valueOf("2000-01-01"),
                                                     Date.valueOf("2010-05-14")
                                            )
                                            .toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime fakeFutureTime() {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.valueOf("2019-06-01"),
                                                     Date.valueOf("2030-01-01")
                                            )
                                            .toInstant(), ZoneId.systemDefault());
    }


    private LocalDateTime fakeFutureTime(int dayOfEvent) {
        Date date = Date.valueOf(LocalDate.now().plusDays(dayOfEvent));

        // in get hour bwetween 8 and 20
        int hour = random.nextInt(15) + 6;
        int minute = fakeMinuteOfTime_15Steps();


        LocalTime localTime = LocalTime.of(hour, minute);
        LocalDate localDate = LocalDate.now().plusDays(dayOfEvent);
        return LocalDateTime.of(localDate, localTime);
    }


    private LocalDateTime fakeTimeAfter(LocalDateTime dateTime) {
        LocalTime localTime;

        int hour, minute;

        // get an hour between 6-22  which is after starting hour of datetime
        do {
            hour = random.nextInt(17) + 6;
        } while(hour <= dateTime.getHour());

        minute = fakeMinuteOfTime_15Steps();

        localTime = LocalTime.of(hour, minute);

        return LocalDateTime.of(dateTime.toLocalDate(), localTime);
    }


    private LocalDate fakeAgeAsLocalDate(int min, int max) {
        return LocalDate.ofInstant(faker.date()
                                        .between(Date.valueOf(LocalDate.now().minusYears(max)),
                                                 Date.valueOf(LocalDate.now().minusYears(min))
                                        )
                                        .toInstant(), ZoneId.systemDefault());
    }


    private Room randomRoom() {
        int daRoom = randomInt(1, 3);
        switch(daRoom) {
            case 1:
                return Room.GroundFloor;
            case 2:
                return Room.Green;
            case 3:
                return Room.Orange;
        }
        return Room.GroundFloor;
    }


    private List<String> randomBirthdayTypes() {
        String[] types = this.birthdayTypes.clone();
        shuffleArray(types);
        int typeCount = randomInt(0, 4);
        List<String> ret = new LinkedList<>();
        for(int i = 0; i <= typeCount; i++) {
            ret.add(types[i]);
        }
        return ret;
    }


    private String randomCourseName() {
        String courseName = "";
        // 0 faecher
        String[] subjects =
            new String[]{ "Mathematik", "Biologie", "Physik", "Chemie", "Mechanik", "Spanisch",
                          "Italienisch", "Französisch", "Russisch", "Chinesisch" };
        String[] description =
            new String[]{ " für Kids", " für schlaue Köpfe", " für Einsteins", " für Weinsteins",
                          " Spaß", " mit Freunden", " für Morgen", " :Make It Great Again",
                          " : Take my money", " leicht gemacht" };

        // 1 kreatives
        String[] creativity = new String[]{ "Rhyme", "Poesie", "Ausdruckstanz", "Malen" };
        String[] like = new String[]{ " like", " gecoached von", " mit", " über" };
        String[] who = new String[]{ " Eminem", " Mao Tse Tung", " Cersei", " Ibrahimovic" };

        // 2 bobo zeugs
        String[] boboActivity =
            new String[]{ "Trading", "Kräuterzucht", "Miteinander", "Klimaretten " };
        String[] boboTypes =
            new String[]{ " lernt sich jung", " für Bobos", " wegen dem Willen deiner Eltern",
                          " Waldorfstyle" };

        int selection = random.nextInt(4);
        int sel1, sel2, sel3;

        switch(selection) {
            // merged cases 0,1 just to have slightly more of the less confusing names
            case 0:
            case 1:
                sel1 = random.nextInt(10);
                sel2 = random.nextInt(10);
                courseName = subjects[sel1] + description[sel2];
                break;
            case 2:
                sel1 = random.nextInt(4);
                sel2 = random.nextInt(4);
                sel3 = random.nextInt(4);
                courseName = creativity[sel1] + like[sel2] + who[sel3];
                break;
            case 3:
                sel1 = random.nextInt(4);
                sel2 = random.nextInt(4);
                courseName = boboActivity[sel1] + boboTypes[sel2];
                break;
        }

        return courseName;
    }


    /**
     * #################
     * # Fake Entities #
     * #################
     */


    public Trainer fakeNewTrainerEntity() {
        Trainer trainer = new Trainer();
        trainer.setBirthday(fakeAgeAsLocalDate(16, 100));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeFirstName());
        trainer.setLastName(fakeLastName());
        trainer.setId(null);
        trainer.setCreated(null);
        trainer.setUpdated(null);
        trainer.setPassword("password123");

        trainer.setBirthdayTypes(randomBirthdayTypes());
        return trainer;
    }


    public Customer fakeNewCustomerEntity() {
        Customer karen = new Customer();
        karen.setId(null);
        karen.setEmail(fakeEmail());
        karen.setPhone(fakePhoneNumber());
        karen.setFirstName(fakeFirstName());
        karen.setLastName(fakeLastName());
        karen.setBirthOfChild(fakeChildBirthday());
        karen.setWantsEmail(true);
        karen.setChildName(fakeFirstName());
        karen.setChildLastName(karen.getLastName());
        return karen;
    }


    public RoomUse fakeRoomUse(int dayOfEvent) {
        RoomUse roomUse = new RoomUse();
        // no of days the room is reserved for this one booking
        int timeSpan = 0;

        roomUse.setBegin(fakeFutureTime(dayOfEvent));
        roomUse.setEnd(fakeTimeAfter(roomUse.getBegin()));
        roomUse.setRoom(randomRoom());
        // set ID
        if(roomUse.getBegin().getHour() < 8 ||
           roomUse.getBegin().getHour() > 22 ||
           roomUse.getEnd().getHour() < 8 ||
           roomUse.getEnd().getHour() > 22) {
            return fakeRoomUse(dayOfEvent);
        } else {
            return roomUse;
        }
    }


    public Event fakeNewRent(int endOfSimulation) {
        Event rent = new Event();
        // rent a room arbitrarily between 3 days from now on and the end of simulation
        int dayOfRent = random.nextInt(endOfSimulation) + 3;

        rent.setEventType(EventType.Rent);

        Set<Customer> customers = new HashSet<>();
        Customer customer = fakeNewCustomerEntity();
        customers.add(customer);
        rent.setCustomers(customers);

        RoomUse roomUse = fakeRoomUse(dayOfRent);
        LinkedList<RoomUse> roomUses = new LinkedList<>();
        roomUses.add(roomUse);
        rent.setRoomUses(roomUses);

        rent.setName("Miete für " +
                     customer.getFirstName().charAt(0) +
                     ". " +
                     customer.getLastName() +
                     "(" +
                     roomUse.getBegin().getYear() +
                     "/" +
                     roomUse.getBegin().getMonth() +
                     "/" +
                     roomUse.getBegin().getDayOfMonth() +
                     ")");

        rent.setId(null);
        return rent;
    }


    public Event fakeConsultationEntity(List<Trainer> availableTrainers, int endOfSimulation) {
        Event consultation = new Event();
        consultationCount++;
        Collections.shuffle(availableTrainers);

        // any time from tomorrow until end of simulation
        int dayOfEvent = random.nextInt(endOfSimulation) + 1;

        consultation.setEventType(EventType.Consultation);
        consultation.setTrainer(availableTrainers.get(0));
        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUse(dayOfEvent));
        consultation.setRoomUses(rooms);

        Set<Customer> customers = new HashSet<>();
        Customer customerDto = fakeNewCustomerEntity();
        customers.add(customerDto);
        consultation.setCustomers(customers);

        consultation.setName("Consultation " +
                             consultationCount +
                             "(" +
                             customerDto.getFirstName().charAt(0) +
                             ". " +
                             customerDto.getLastName() +
                             ")");

        return consultation;
    }



    public Event fakeNewBirthdayEntity(List<Trainer> availableTrainer, int endOfSimulation) {
        Event event = new Event();

        String[] types = birthdayTypes.clone();
        // any day from 3 days in future intil end of simulation
        int dayOfBirthday = random.nextInt(endOfSimulation) + 3;

        List<RoomUse> roomUses = new LinkedList<>();
        roomUses.add(fakeRoomUse(dayOfBirthday));
        event.setRoomUses(roomUses);

        event.setAgeToBe(randomInt(5, 14));
        shuffleArray(types);
        event.setBirthdayType(types[0]);
        event.setEventType(EventType.Birthday);
        event.setId(null);
        event.setHeadcount(randomInt(5, 18));

        event.setCreated(null);
        event.setUpdated(null);

        Set<Customer> customers = new HashSet<>();
        Customer customer = fakeNewCustomerEntity();
        Set<Event> events = new HashSet<>();
        events.add(event);
        customer.setEvents(events);
        customers.add(customer);
        event.setCustomers(customers);

        event.setName(event.getBirthdayType() + ": " + customer.getFirstName() + " " + customer.getLastName());
        return event;
    }


    public Event fakeNewCourseEntity(List<Trainer> availableTrainer, int simulatedDays) {
        Event event = new Event();

        // choose a dat between 3 days  from now and timespan until end of simulation
        int dayOfEvent = random.nextInt(simulatedDays) + 3;
        // end of application anytime before start of event
        int endOfApplication = random.nextInt(dayOfEvent);

        Collections.shuffle(availableTrainer);
        int minAge = randomInt(6, 12);
        int maxAge;

        do {
            maxAge = randomInt(6, 14);
        } while(maxAge < minAge);

        List<RoomUse> roomUses = new LinkedList<>();
        roomUses.add(fakeRoomUse(dayOfEvent));
        event.setRoomUses(roomUses);
        event.setName(randomCourseName());
        event.setDescription(faker.regexify("[a-zA-Z]{20,100}"));
        event.setCreated(null);
        event.setUpdated(null);
        event.setMinAge(minAge);
        event.setMaxAge(maxAge);
        event.setPrice((double) ( random.nextInt(100) + 8 ));
        event.setMaxParticipants(random.nextInt(5) + 6);
        event.setEventType(EventType.Course);
        event.setTrainer(availableTrainer.get(0));
        LocalDateTime now = LocalDateTime.now();
        event.setEndOfApplication(now.plusDays(endOfApplication));
        String tag[] = {"Math", "Science", "Expirements", "Dance", "Painting", "Art", "Programming"};
        String set = tag[randomInt(0,6)];
        event.setEvent_tags(set);
        event.setId(null);
        event.setDeleted(false);
        return event;
    }


    public static void shuffleArray(String[] ar) {
        Random rnd = new Random();
        for(int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
