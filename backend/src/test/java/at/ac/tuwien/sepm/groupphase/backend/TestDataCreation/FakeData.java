package at.ac.tuwien.sepm.groupphase.backend.TestDataCreation;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.Room;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import com.github.javafaker.Faker;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class FakeData {
    Faker faker;
    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";

    public FakeData(){
        faker = new Faker(new Locale("de-DE"));
    }

    public String fakeEmail(){
        return faker.internet().emailAddress();
    }

    public String fakePhoneNumber(){
        return faker.phoneNumber().phoneNumber();
    }

    public String fakeFirstName(){
        return faker.name().firstName();
    }

    public String fakeLastName(){
        return faker.name().lastName();
    }


    public Long fakeID(){
        return (long)faker.number().numberBetween(0,1000000);
    }

    public LocalDateTime fakePastTimeAfter2000(){
        return LocalDateTime.ofInstant(faker.date().between(Date.valueOf("2000-01-01"), Date.valueOf("2019-05-14")).toInstant(), ZoneId.systemDefault());
    }

    public LocalDateTime fakeFutureTime(){
        return LocalDateTime.ofInstant(faker.date().between(Date.valueOf("2019-06-01"), Date.valueOf("2025-01-01")).toInstant(), ZoneId.systemDefault());

    }
    public LocalDateTime fakeTimeAfter(LocalDateTime dateTime){
        return LocalDateTime.ofInstant(faker.date().between(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()), Date.valueOf("2025-01-01")).toInstant(), ZoneId.systemDefault());
    }
    public int fakeAge(int min, int max){
        return faker.number().numberBetween(min, max);
    }

    public TrainerDto fakeTrainer(){
        TrainerDto trainer = new TrainerDto();
        trainer.setAge(fakeAge(16, 100));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeFirstName());
        trainer.setLastName(fakeLastName());
        trainer.setId(fakeID());
        trainer.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found){
            updated = fakePastTimeAfter2000();
            if(trainer.getCreated().isBefore(updated)){
                found = true;
            }
        }
        trainer.setUpdated(updated);
        return trainer;
    }

    public int randomInt(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    public Room randomRoom(){
        int daRoom = randomInt(1,3);
        switch(daRoom){
            case 1:
                return Room.GroundFloor;
            case 2:
                return Room.Green;
            case 3:
                return Room.Orange;
        }
        return Room.GroundFloor;
    }

    public CustomerDto fakeCustomer(){
        CustomerDto karen = new CustomerDto();
        karen.setId(fakeID());
        karen.setEmail(fakeEmail());
        karen.setPhone(fakePhoneNumber());
        karen.setFirstName(fakeFirstName());
        karen.setLastName(fakeLastName());
        return karen;
    }

    public RoomUse fakeRoomUseDto(){
        RoomUse roomUse = new RoomUse();
        roomUse.setBegin(fakeFutureTime());
        roomUse.setEnd(fakeTimeAfter(roomUse.getBegin()));
        roomUse.setRoom(randomRoom());
        return roomUse;
    }

    public Trainer fakeTrainerE(){
        Trainer trainer = new Trainer();
        trainer.setAge(fakeAge(16, 100));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeFirstName());
        trainer.setLastName(fakeLastName());
        trainer.setId(fakeID());
        trainer.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found){
            updated = fakePastTimeAfter2000();
            if(trainer.getCreated().isBefore(updated)){
                found = true;
            }
        }
        trainer.setUpdated(updated);
        return trainer;
    }

    public EventDto fakeBirthday(){
        EventDto bday = new EventDto();
        bday.setAgeToBe(fakeAge(5,19));
        Set<CustomerDto> customers = new HashSet<>();
        customers.add(fakeCustomer());
        bday.setCustomerDtos(customers);
        bday.setHeadcount(fakeAge(2,10));
        bday.setTrainer(fakeTrainer());
        bday.setBirthdayType(BirthdayType.Rocket);
        List<RoomUse> rooms = new LinkedList<>();
        rooms.add(fakeRoomUseDto());
        bday.setRoomUses(rooms);
        bday.setCreated(fakePastTimeAfter2000());
        boolean found = false;
        LocalDateTime updated = null;
        while(!found){
            updated = fakePastTimeAfter2000();
            if(bday.getCreated().isBefore(updated)){
                found = true;
            }
        }
        bday.setUpdated(updated);
        bday.setEventType(EventType.Birthday);
        bday.setName("Sickes Geburtstag oida");
        return bday;
    }


    public EventDto fakeCourse(){
        EventDto course = new EventDto();
        course.setEventType(EventType.Course);
        course.setName("Kurs für Hs plays iq 200");
        course.setPrice(25.0);
        Set<CustomerDto> customers = new HashSet<>();

        customers.add(fakeCustomer());
        course.setCustomerDtos(customers);
        course.setTrainer(fakeTrainer());

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

}
