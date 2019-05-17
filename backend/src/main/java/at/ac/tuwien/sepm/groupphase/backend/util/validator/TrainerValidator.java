package at.ac.tuwien.sepm.groupphase.backend.util.validator;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class TrainerValidator implements IValidator<Trainer> {

    @Override
    public void validate (Trainer entity) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        // matches any kind of regular phone number format
        // e.g. 0660 123 45 67, +(43) 01 234-56-78
        String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Pattern emailPattern = Pattern.compile(emailRegex);


        if (entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("first name must be set");
        }

        if (entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("last name must be set");
        }

      /*  if (entity.getAge() ==  null || (entity.getAge() < 16 || entity.getAge() > 120)) {
            throw new InvalidEntityException("age must be specified and must be a reasonable value");
        }*/

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
