package at.ac.tuwien.sepm.groupphase.backend.TestDataCreation;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import com.github.javafaker.Faker;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class FakeData {
    Faker faker;
    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";


    public FakeData() {
        faker = new Faker(new Locale("de-DE"));
    }


    public String fakeEmail() {
        return faker.internet().emailAddress();
    }


    public String fakePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }


    public String fakeFirstName() {
        return faker.name().firstName();
    }


    public String fakeLastName() {
        return faker.name().lastName();
    }


    public Long fakeID() {
        return (long) faker.number().numberBetween(0, 1000000);
    }


    public LocalDateTime fakePastTimeAfter2000() {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.valueOf("2000-01-01"),
                                                     Date.valueOf("2019-05-14")
                                            )
                                            .toInstant(), ZoneId.systemDefault());
    }


    public LocalDateTime fakeFutureTime() {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.valueOf("2019-06-01"),
                                                     Date.valueOf("2030-01-01")
                                            )
                                            .toInstant(), ZoneId.systemDefault());
    }


    public LocalDateTime fakeTimeAfter(LocalDateTime dateTime) {
        return LocalDateTime.ofInstant(faker.date()
                                            .between(Date.from(
                                                dateTime.atZone(ZoneId.systemDefault())
                                                        .toInstant()), Date.valueOf("2040-01-01"))
                                            .toInstant(), ZoneId.systemDefault());
    }


    public int fakeAge(int min, int max) {
        return faker.number().numberBetween(min, max);
    }


    public LocalDate fakeAgeAsLocalDate(int min, int max) {
        return LocalDate.ofInstant(faker.date()
                                        .between(Date.valueOf(LocalDate.now().minusYears(max)),
                                                 Date.valueOf(LocalDate.now().minusYears(min))
                                        )
                                        .toInstant(), ZoneId.systemDefault());
    }


    public TrainerDto fakeTrainerDto() {
        TrainerDto trainer = new TrainerDto();
        trainer.setBirthday(fakeAgeAsLocalDate(16, 100));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeFirstName());
        trainer.setLastName(fakeLastName());
        List<String> birthdayTypes = new LinkedList<>();
        birthdayTypes.add("Rocket");
        trainer.setBirthdayTypes(birthdayTypes);
        trainer.setId(fakeID());
        trainer.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found) {
            updated = fakePastTimeAfter2000();
            if(trainer.getCreated().isBefore(updated)) {
                found = true;
            }
        }
        trainer.setUpdated(updated);
        return trainer;
    }


    public Trainer fakeTrainerEntity() {
        Trainer trainer = new Trainer();
        trainer.setBirthday(fakeAgeAsLocalDate(16, 100));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeFirstName());
        trainer.setLastName(fakeLastName());
        trainer.setId(fakeID());
        trainer.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found) {
            updated = fakePastTimeAfter2000();
            if(trainer.getCreated().isBefore(updated)) {
                found = true;
            }
        }
        trainer.setUpdated(updated);
        return trainer;
    }


    public int randomInt(int min, int max) {
        return (int) ( Math.random() * ( ( max - min ) + 1 ) ) + min;
    }


    public Room randomRoom() {
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


    public CustomerDto fakeCustomer() {
        CustomerDto karen = new CustomerDto();
        karen.setId(fakeID());
        karen.setEmail(fakeEmail());
        karen.setPhone(fakePhoneNumber());
        karen.setFirstName(fakeFirstName());
        karen.setLastName(fakeLastName());
        return karen;
    }


    public Customer fakeNewCustomerEntity() {
        Customer karen = new Customer();
        karen.setId(null);
        karen.setEmail(fakeEmail());
        karen.setPhone(fakePhoneNumber());
        karen.setFirstName(fakeFirstName());
        karen.setLastName(fakeLastName());
        return karen;
    }


    public Customer fakeCustomerEntity() {
        Customer karen = new Customer();
        karen.setId(fakeID());
        karen.setEmail(fakeEmail());
        karen.setPhone(fakePhoneNumber());
        karen.setFirstName(fakeFirstName());
        karen.setLastName(fakeLastName());
        return karen;
    }


    public RoomUse fakeRoomUseDto() {
        RoomUse roomUse = new RoomUse();
        roomUse.setBegin(fakeFutureTime());
        roomUse.setEnd(fakeTimeAfter(roomUse.getBegin()));
        roomUse.setRoom(randomRoom());
        roomUse.setEnd(roomUse.getBegin().plusDays(1));
        if(roomUse.getBegin().getHour() < 8 ||
           roomUse.getBegin().getHour() > 22 ||
           roomUse.getEnd().getHour() < 8 ||
           roomUse.getEnd().getHour() > 22) {
            return fakeRoomUseDto();
        } else {
            return roomUse;
        }
    }


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

        trainer.setBirthdayTypes(randomBirthdayTypes());
        return trainer;
    }


    public TrainerDto fakeNewTrainerDto() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setBirthday(fakeAgeAsLocalDate(16, 100));
        trainerDto.setEmail(fakeEmail());
        trainerDto.setPhone(fakePhoneNumber());
        trainerDto.setFirstName(fakeFirstName());
        trainerDto.setLastName(fakeLastName());
        trainerDto.setId(null);
        trainerDto.setCreated(null);
        trainerDto.setUpdated(null);

        trainerDto.setBirthdayTypes(randomBirthdayTypes());
        return trainerDto;
    }


    public EventDto fakeBirthday() {
        EventDto bday = new EventDto();
        bday.setAgeToBe(fakeAge(5, 19));
        Set<CustomerDto> customers = new HashSet<>();
        customers.add(fakeCustomer());
        bday.setCustomerDtos(customers);
        bday.setHeadcount(fakeAge(5, 10));
        bday.setTrainer(fakeTrainerDto());
        bday.setBirthdayType("Rocket");
        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUseDto());
        bday.setRoomUses(rooms);
        bday.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found) {
            updated = fakePastTimeAfter2000();
            if(bday.getCreated().isBefore(updated)) {
                found = true;
            }
        }
        bday.setUpdated(updated);
        bday.setEventType(EventType.Birthday);
        bday.setName("Sickes Geburtstag oida");
        return bday;
    }


    public List<String> randomBirthdayTypes() {
        String[] types = { "Rocket", "Dryice", "Superhero", "Photography", "Painting" };
        shuffleArray(types);
        int typeCount = randomInt(0, 4);
        List<String> ret = new LinkedList<>();
        for(int i = 0; i <= typeCount; i++) {
            ret.add(types[i]);
        }
        return ret;
    }


    public EventDto fakeCourse() {
        EventDto course = new EventDto();
        course.setEventType(EventType.Course);
        course.setName("Kurs für Hs plays iq 200");
        course.setPrice(25.0);
        Set<CustomerDto> customers = new HashSet<>();

        customers.add(fakeCustomer());
        course.setCustomerDtos(customers);
        course.setTrainer(fakeTrainerDto());

        course.setDescription("Leeroy mecanics");
        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUseDto());
        course.setRoomUses(rooms);

        LocalDateTime now = LocalDateTime.now();
        course.setCreated(now);
        course.setUpdated(now);


        course.setEndOfApplication(now.plusDays(3));
        course.setMaxParticipant(20);
        course.setMinAge(3);
        return course;
    }


    public EventDto fakeRent() {
        EventDto rent = new EventDto();

        rent.setEventType(EventType.Rent);

        Set<CustomerDto> customerDto = new HashSet<>();
        customerDto.add(fakeCustomer());
        rent.setCustomerDtos(customerDto);

        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUseDto());
        rent.setRoomUses(rooms);

        rent.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found) {
            updated = fakePastTimeAfter2000();
            if(rent.getCreated().isBefore(updated)) {
                found = true;
            }
        }
        rent.setUpdated(updated);

        return rent;
    }


    public EventDto fakeConsultation() {
        EventDto consultation = new EventDto();
        consultation.setName("Consultation1");
        consultation.setEventType(EventType.Consultation);
        consultation.setTrainer(fakeTrainerDto());
        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUseDto());
        consultation.setRoomUses(rooms);
        Set<CustomerDto> customers = new HashSet<>();
        customers.add(fakeCustomer());
        consultation.setCustomerDtos(customers);
        return consultation;
    }


    public Event fakeNewBirthdayEntity() {
        Event event = new Event();
        List<RoomUse> roomUses = new LinkedList<>();
        roomUses.add(fakeRoomUseDto());
        event.setAgeToBe(randomInt(5, 14));
        String[] types = { "Rocket", "Dryice", "Superhero", "Photography", "Painting" };
        shuffleArray(types);
        event.setBirthdayType(types[0]);
        event.setEventType(EventType.Birthday);
        event.setId(null);
        event.setHeadcount(randomInt(5, 18));
        event.setName("This is a birthday");
        event.setCreated(null);
        event.setUpdated(null);
        event.setRoomUses(roomUses);
        Set<Customer> customers = new HashSet<>();
        Customer customer = fakeNewCustomerEntity();
        Set<Event> events = new HashSet<>();
        events.add(event);
        customer.setEvents(events);
        customers.add(customer);
        event.setCustomers(customers);
        return event;
    }


    public Event fakeBirthdayEntity() {
        Event event = new Event();
        List<RoomUse> roomUses = new LinkedList<>();
        roomUses.add(fakeRoomUseDto());
        event.setAgeToBe(randomInt(5, 14));
        String[] types = { "Rocket", "Dryice", "Superhero", "Photography", "Painting" };
        shuffleArray(types);
        event.setBirthdayType(types[0]);
        event.setEventType(EventType.Birthday);
        event.setId(fakeID());
        event.setHeadcount(randomInt(5, 18));
        event.setName("This is a birthday");
        event.setCreated(fakePastTimeAfter2000());
        event.setUpdated(event.getCreated());
        event.setRoomUses(roomUses);
        Set<Customer> customers = new HashSet<>();
        customers.add(fakeNewCustomerEntity());
        return event;
    }


    static void shuffleArray(String[] ar) {
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
