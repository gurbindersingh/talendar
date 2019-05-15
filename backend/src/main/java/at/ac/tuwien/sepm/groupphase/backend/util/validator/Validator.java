package at.ac.tuwien.sepm.groupphase.backend.util.validator;

import at.ac.tuwien.sepm.groupphase.backend.pojo.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    public Validator(){}

    public void validateHoliday (Holiday holiday) throws InvalidEntityException {
    }
}
