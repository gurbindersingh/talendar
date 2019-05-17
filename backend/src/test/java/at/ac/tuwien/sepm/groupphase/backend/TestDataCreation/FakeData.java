package at.ac.tuwien.sepm.groupphase.backend.TestDataCreation;

import at.ac.tuwien.sepm.groupphase.backend.TestObjects.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import com.github.javafaker.Faker;

import java.sql.Date;
import java.time.LocalDate;
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

    public LocalDate fakeAge(int min, int max){
        return LocalDate.ofInstant(faker.date().between(Date.valueOf(LocalDate.now().minusYears(max)), Date.valueOf(LocalDate.now().minusYears(min))).toInstant(), ZoneId.systemDefault());
    }

    public TrainerDto fakeTrainerDto (){
        TrainerDto trainer = new TrainerDto();
        trainer.setBirthday(fakeAge(16, 100));
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


    public Trainer fakeTrainerEntity() {
        Trainer trainer = new Trainer();
        trainer.setBirthday(fakeAge(16, 100));
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
