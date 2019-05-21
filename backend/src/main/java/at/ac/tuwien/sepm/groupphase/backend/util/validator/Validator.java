package at.ac.tuwien.sepm.groupphase.backend.util.validator;



import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Course;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class Validator{
    // matches any kind of regular phone number format
    // e.g. 0660 123 45 67, +(43) 01 234-56-78
    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    Pattern phonePattern = Pattern.compile(phoneRegex);
    Pattern emailPattern = Pattern.compile(emailRegex);


    public Validator(){}


    public void validateCourse(Course course) throws InvalidEntityException{


        if(course.getName() == null || course.getName().isBlank()){
            throw new InvalidEntityException("Name not set");
        }

        if(course.getDescription() == null || course.getDescription().isBlank()){
            throw new InvalidEntityException("Description not set");
        }

        if(course.getMaxParticipants() == null){
            throw new InvalidEntityException("Maximal participants not set");
        } else if(course.getMaxParticipants() < 5){
            throw new InvalidEntityException("Maximal participants has to be above 5");
        }

        for(int i = 0; i < course.getRoomUses().size(); i++) {
            if(course.getRoomUses().get(i).getBegin().isAfter(course.getEndOfApplication())){
                throw new InvalidEntityException("Date of end of application is after begin of course");
            }
        }

        if(course.getCustomer() != null){
            throw new InvalidEntityException("There should be no customers in creating a course");
        }

        try{
            validateTrainer(course.getTrainer());
        } catch(InvalidEntityException e){
            throw e;
        }

        if(course.getPrice() == null){
            throw new InvalidEntityException("Price not set");
        }

    }


    public void validateBirthday(Birthday birthday) throws InvalidEntityException{
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
        if(birthday.getHeadCount() < 0){
            throw new InvalidEntityException("Head Count cannot be less than 0");
        }
        if(birthday.getAvgAge() < 0 || birthday.getAvgAge() > 20){
            throw new InvalidEntityException("Average age invalid");
        }

        try{
            validateCustomer(birthday.getGuardian());
        }catch(InvalidEntityException e){
            throw e;
        }

        try{
            validateTrainer(birthday.getTrainer());
        }catch(InvalidEntityException e){
            throw e;
        }



    }

    public void validateCustomer(Customer entity) throws InvalidEntityException{
        if (entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("a valid phone number must be set");
        }

        if (entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("a valid email address must be set");
        }
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
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
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before latest update time");}
    }

    public void validateHoliday (Holiday holiday) throws InvalidEntityException {

        if (holiday.getId() != null) {
            throw new InvalidEntityException("id must be null");
        }
        if (holiday.getTrainerid() == null) {
            throw new InvalidEntityException("Trainerid must not be null");
        }
        if (holiday.getHolidayStart().isAfter(holiday.getHolidayEnd())) {
            throw new InvalidEntityException("Start of holiday must be before the end");
        }
        if (holiday.getHolidayStart().isBefore(LocalDateTime.now())) {
            throw new InvalidEntityException("Holidays can only take place in the future");
        }
    }
}
