package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.persistence.HolidayRepository;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class HolidayService implements IHolidayService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayRepository holidayRepository;
    private final TrainerRepository trainerRepository;
    private final Validator validate;

    @Autowired
    public HolidayService (HolidayRepository holidayRepository, TrainerRepository trainerRepository, Validator validate) {
        this.holidayRepository = holidayRepository;
        this.trainerRepository = trainerRepository;
        this.validate = validate;
    }


    @Override
    public Holiday save (Holiday holiday) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new holiday: {}", holiday);
        LOGGER.info("Test");

        try {
            validate.validateHoliday(holiday);
            if(!this.trainerRepository.existsById(holiday.getTrainer().getId())){
                InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
                LOGGER.error("attempt to save holiday with trainer that doesnt exist");
                throw new ValidationException(e.getMessage(), e);
            }
        }
        catch(InvalidEntityException e) {
            LOGGER.error("validity violation: {}", e.getMessage(), e);
            throw new ValidationException(e.getMessage(), e);
        }

        try {
            return holidayRepository.save(holiday);
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }
}
