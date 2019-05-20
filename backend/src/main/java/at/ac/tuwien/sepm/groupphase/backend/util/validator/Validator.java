package at.ac.tuwien.sepm.groupphase.backend.util.validator;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class Validator{
    // matches any kind of regular phone number format
    // e.g. 0660 123 45 67, +(43) 01 234-56-78
    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    Pattern phonePattern = Pattern.compile(phoneRegex);
    Pattern emailPattern = Pattern.compile(emailRegex);



    public void validateCourse(Event course) throws InvalidEntityException{
        LocalDateTime now = LocalDateTime.now();

        if(course.getName() == null || course.getName().isBlank()){
            throw new InvalidEntityException("Name cannot be empty");
        }
        if (course.getCreated() == null || course.getCreated().isAfter(now)) {
            throw new InvalidEntityException("creation time must be set (past date)");
        }
        if (course.getUpdated() == null || course.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("lates update time must be set (past date)");
        }
        if (course.getCreated().isAfter(course.getUpdated())) {
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before latest update time");
        }

        if(course.getEndOfApplication() == null){
            throw new InvalidEntityException("End of application is not set");
        } else if(course.getEndOfApplication().isBefore(now)){
            throw new InvalidEntityException("End of application should be in future");
        }

        if(course.getPrice() == null){
            throw new InvalidEntityException("Price not set");
        } else if(course.getPrice() < 0){
            throw new InvalidEntityException("Price cant be negative");
        }

        if(course.getMaxParticipant() == null){
            throw new InvalidEntityException("Maximum participant is not set");
        } else if(course.getMaxParticipant() < 5){
            throw new InvalidEntityException("Maximum participant should be bigger than 5");
        }

        if(course.getMinAge() != null && course.getMaxAge() != null) {
            if(course.getMinAge() < 0 || course.getMaxAge() > 100 || course.getMinAge() > course.getMaxAge()) {
                throw new InvalidEntityException("Min or max age is invalid");
            }
        } else if(course.getMinAge() != null && course.getMinAge() < 0){
            throw new InvalidEntityException("Min cant be smaller than 0");
        } else if(course.getMaxAge() != null && course.getMaxAge() > 100){
            throw new InvalidEntityException("Min cant be greater than 100");
        }

        if(course.getDescription() == null || course.getDescription().isBlank()){
            throw new InvalidEntityException("Description is not set");
        }

        if(course.getTrainer() == null){
            throw new InvalidEntityException("Trainer is not set");
        } else{
            try{
                this.validateTrainer(course.getTrainer());
            } catch(InvalidEntityException e){
                throw e;
            }
        }

        if(course.getCustomers() != null){
            throw new InvalidEntityException("Customer list should be null");
        }

    }



    public void validateBirthday(Event birthday) throws InvalidEntityException{
        if(birthday.getEventType() != EventType.Birthday){
            throw new InvalidEntityException("This is was supposed to be a birthday");
        }
        LocalDateTime now = LocalDateTime.now();
        if(birthday.getName() == null || birthday.getName().isBlank()){
            throw new InvalidEntityException("Name cannot be empty");
        }
        if(birthday.getTrainer() == null){
            throw new InvalidEntityException("Trainer Invalid");
        }
        if (birthday.getCreated() == null || birthday.getCreated().isAfter(now)) {
            throw new InvalidEntityException("creation time must be set (past date)");
        }
        if (birthday.getUpdated() == null || birthday.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("lates update time must be set (past date)");
        }
        if (birthday.getCreated().isAfter(birthday.getUpdated())) {
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before latest update time");
        }
        if(birthday.getHeadcount() < 0){
            throw new InvalidEntityException("Head Count cannot be less than 0");
        }
        if(birthday.getAgeToBe() < 0 || birthday.getAgeToBe() > 20){
            throw new InvalidEntityException("Average age invalid");
        }
        if(birthday.getCustomers().size() != 1){
            throw new InvalidEntityException("Too many or too little customers for a birthday");
        }

        try{
            for(Customer x: birthday.getCustomers()
                ) {
                validateCustomer(x);
            }
        }catch(InvalidEntityException e){
            throw e;
        }

        try{
            for(RoomUse r:birthday.getRoomUses()
                ) {
                validateRoomUse(r);
            }
        }catch(InvalidEntityException e){
            throw e;
        }

        try{
            validateTrainer(birthday.getTrainer());
        }catch(InvalidEntityException e){
            throw e;
        }



    }
    public void validateRoomUse(RoomUse entity) throws InvalidEntityException{
        if(entity.getBegin().isAfter(entity.getEnd())){
            throw new InvalidEntityException("The End of the use cannot be later than the begining");
        }
    }

    public void validateCustomer(Customer entity) throws InvalidEntityException{
        if (entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("a valid phone number must be set");
        }

        if (entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("a valid email address must be set");
        }
        if (entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
        }
        if (entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("last name must be set");
        }
    }

    public void validateTrainer(Trainer entity) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        if (entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
        }

        if (entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("last name must be set");
        }

        if (entity.getAge() ==  null || (entity.getAge() < 16 || entity.getAge() > 120)) {
            throw new InvalidEntityException("age must be specified and must be a reasonable value");
        }

        if (entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("a valid phone number must be set");
        }

        if (entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("a valid email address must be set");
        }

        if (entity.getCreated() == null || entity.getCreated().isAfter(now)) {
            throw new InvalidEntityException("creation time must be set (past date)");
        }

        if (entity.getUpdated() == null || entity.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("lates update time must be set (past date)");
        }

        if (entity.getCreated().isAfter(entity.getUpdated())) {
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before latest update time");
        }
    }
}
