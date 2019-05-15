package at.ac.tuwien.sepm.groupphase.backend.TestDataCreation;

import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

public class FakeData {
    FakeValuesService fakeValuesService;
    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public FakeData(){
        fakeValuesService = new FakeValuesService(new Locale("de-DE"),new RandomService());
    }

    public String fakeEmail(){
        return fakeValuesService.regexify(emailRegex);
    }

    public String fakePhoneNumber(){
        return fakeValuesService.regexify(phoneRegex);
    }

    public String fakeName(){
        return fakeValuesService.regexify("[A-Z]([a-z']+){1,20}");
    }

    public Long fakeID(){
        return Long.parseLong(fakeValuesService.regexify("([1-9]+){1,30}"));
    }
    public LocalDateTime fakePastTimeAfter2000(){

        return LocalDateTime.parse(fakeValuesService.regexify("[20](0[0-9]|1[0-8])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])-(0[0-9]|1[0-9]|2[0-3])-(0[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9])-(0[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9]).[0-9]{3}"));


    }
    public TrainerDto fakeTrainer(){
        TrainerDto trainer = new TrainerDto();
        trainer.setAge(Integer.parseInt(fakeValuesService.numerify("##")));
        trainer.setEmail(fakeEmail());
        trainer.setPhone(fakePhoneNumber());
        trainer.setFirstName(fakeName());
        trainer.setLastName(fakeName());
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
