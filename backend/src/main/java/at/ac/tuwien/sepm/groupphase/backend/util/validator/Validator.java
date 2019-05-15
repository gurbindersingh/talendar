package at.ac.tuwien.sepm.groupphase.backend.util.validator;

import at.ac.tuwien.sepm.groupphase.backend.pojo.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    public Validator(){}

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
    }
}
