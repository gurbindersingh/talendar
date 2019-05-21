package at.ac.tuwien.sepm.groupphase.backend.TestDataCreation;

import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.Room;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.RoomUseDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.tomcat.jni.Local;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;


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
        return LocalDateTime.ofInstant(faker.date().between(Date.valueOf("2019-10-15"), Date.valueOf("2029-05-14")).toInstant(), ZoneId.systemDefault());
    }

    public int fakeAge(int min, int max){
        return faker.number().numberBetween(min, max);
    }


    public RoomUseDto fakeRoomUse(){
        RoomUseDto roomUseDto = new RoomUseDto();
        roomUseDto.setId(fakeID());
        LocalDateTime localDateTime = fakeFutureTime();
        roomUseDto.setBegin(localDateTime);
        roomUseDto.setEnd(localDateTime.plusDays(3));
        roomUseDto.setRoom(Room.Green);
        roomUseDto.setEvent(null);
        return roomUseDto;
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

}
