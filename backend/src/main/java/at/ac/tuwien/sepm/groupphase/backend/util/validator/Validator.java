package at.ac.tuwien.sepm.groupphase.backend.util.validator;



import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class Validator{
    // matches any kind of regular phone number format
    // e.g. 0660 123 45 67, +(43) 01 234-56-78
    private String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    private String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private Pattern phonePattern = Pattern.compile(phoneRegex);
    private Pattern emailPattern = Pattern.compile(emailRegex);

    public void validateEvent (Event event) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        // Validation for common stuff
        if(event.getCreated() == null || event.getCreated().isAfter(now)) {
            throw new InvalidEntityException("creation time must be set (past date)");
        }
        if(event.getUpdated() == null || event.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("last update time must be set (past date)");
        }
        if(event.getCreated().isAfter(event.getUpdated())) {
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before last update time");
        }

        // Validator for Birthdays
        if(event.getEventType() == EventType.Birthday) {
            if(event.getHeadcount() < 0 || event.getHeadcount() > 20) {
                throw new InvalidEntityException("Head Count cannot be less than 0 or more than 20");
            }
            if(event.getAgeToBe() < 0 || event.getAgeToBe() > 20) {
                throw new InvalidEntityException("Average age invalid");
            }
            if(event.getCustomers().size() != 1) {
                throw new InvalidEntityException("Too many or too little customers for a birthday");
            }

            try {
                for(Customer x : event.getCustomers()
                ) {
                    validateCustomer(x);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }

            try {
                for(RoomUse r : event.getRoomUses()
                ) {
                    validateRoomUse(r);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }

            try {
                validateTrainer(event.getTrainer());
            }
            catch(InvalidEntityException e) {
                throw e;
            }
        }

        // Validator for Consultation
        if(event.getEventType() == EventType.Consultation) {

        }

        // Validator for Course
        if(event.getEventType() == EventType.Course) {
            if(event.getName() == null || event.getName().isBlank()) {
                throw new InvalidEntityException("Name cannot be empty");
            }
            if(event.getEndOfApplication() == null) {
                throw new InvalidEntityException("End of application is not set");
            }
            else if(event.getEndOfApplication().isBefore(now)) {
                throw new InvalidEntityException("End of application should be in future");
            }

            if(event.getPrice() == null) {
                throw new InvalidEntityException("Price not set");
            }
            else if(event.getPrice() < 0) {
                throw new InvalidEntityException("Price cant be negative");
            }

            if(event.getMaxParticipant() == null) {
                throw new InvalidEntityException("Maximum participant is not set");
            }
            else if(event.getMaxParticipant() < 5) {
                throw new InvalidEntityException("Maximum participant should be bigger than 5");
            }

            if(event.getMinAge() != null && event.getMaxAge() != null) {
                if(event.getMinAge() < 0 || event.getMaxAge() > 100 || event.getMinAge() > event.getMaxAge()) {
                    throw new InvalidEntityException("Min or max age is invalid");
                }
            }
            else if(event.getMinAge() != null && event.getMinAge() < 0) {
                throw new InvalidEntityException("Min cant be smaller than 0");
            }
            else if(event.getMaxAge() != null && event.getMaxAge() > 100) {
                throw new InvalidEntityException("Min cant be greater than 100");
            }

            if(event.getDescription() == null || event.getDescription().isBlank()) {
                throw new InvalidEntityException("Description is not set");
            }

            if(event.getTrainer() == null) {
                throw new InvalidEntityException("Trainer is not set");
            }
            else {
                try {
                    this.validateTrainer(event.getTrainer());
                }
                catch(InvalidEntityException e) {
                    throw e;
                }
            }

            if(event.getCustomers() != null) {
                throw new InvalidEntityException("Customer list should be null");
            }
        }

        // Validator for Rent
        if(event.getEventType() == EventType.Rent) {

            if(event.getCustomers().size() != 1) {
                throw new InvalidEntityException("A Rent cannot have more or less than 1 renter.");
            }
            try {
                for(Customer c : event.getCustomers()) {
                    validateCustomer(c);
                }
            }
            catch(InvalidEntityException ie) {
                throw ie;
            }
        }
    }


    public void validateRoomUse (RoomUse entity) throws InvalidEntityException {
        if(entity.getBegin().isAfter(entity.getEnd())) {
            throw new InvalidEntityException("The End of the use cannot be later than the begining");
        }
        if(entity.getBegin().getHour() < 8 || entity.getBegin().getHour() > 22) {
            throw new InvalidEntityException("The beginning of this event is not during work hours");
        }
        if(entity.getEnd().getHour() < 8 || entity.getEnd().getHour() > 22) {
            throw new InvalidEntityException("The end of this event is not during work hours");
        }
    }


    public void validateCustomer (Customer entity) throws InvalidEntityException {
        if(entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("a valid phone number must be set");
        }

        if(entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("a valid email address must be set");
        }
        if(entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
        }
        if(entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("last name must be set");
        }
    }


    public void validateTrainer (Trainer entity) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        if(entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
        }

        if(entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("last name must be set");
        }

        if(entity.getBirthday() == null || entity.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidEntityException("age must be specified and a past date");
        }
        else if(( ( LocalDate.now().getYear() - entity.getBirthday().getYear() ) < 16 ) || ( ( LocalDate.now().getYear() - entity.getBirthday().getYear() ) > 120 )) {
            throw new InvalidEntityException("age must be a reasonable value");
        }

        if(entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("a valid phone number must be set");
        }

        if(entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("a valid email address must be set");
        }

        if(entity.getCreated() == null || entity.getCreated().isAfter(now)) {
            throw new InvalidEntityException("creation time must be set (past date)");
        }

        if(entity.getUpdated() == null || entity.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("lates update time must be set (past date)");
        }

        if(entity.getCreated().isAfter(entity.getUpdated())) {
            throw new InvalidEntityException("create time may not be changed afterwards and has to be before latest update time");
        }
    }


    public void validateHoliday (Holiday holiday) throws InvalidEntityException {
        if(holiday.getTrainer() == null) {
            throw new InvalidEntityException("Trainer must not be null");
        }
        if(holiday.getId() != null) {
            throw new InvalidEntityException("id must be null");
        }
        if(holiday.getTrainer().getId() == null) {
            throw new InvalidEntityException("Trainerid must not be null");
        }
        if(holiday.getHolidayStart().isAfter(holiday.getHolidayEnd())) {
            throw new InvalidEntityException("Start of holiday must be before the end");
        }
        if(holiday.getHolidayStart().isBefore(LocalDateTime.now())) {
            throw new InvalidEntityException("Holidays can only take place in the future");
        }
    }
}
