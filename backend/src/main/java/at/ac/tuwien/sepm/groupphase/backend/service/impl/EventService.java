package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.persistence.BirthdayRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.CourseRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class EventService implements IEventService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);


    private final BirthdayRepository birthdayRepository;
    private final CourseRepository courseRepository;
    private final Validator validator;

    @Autowired
    public EventService(BirthdayRepository birthdayRepository, CourseRepository courseRepository, Validator validator){
        this.validator = validator;
        this.courseRepository = courseRepository;
        this.birthdayRepository = birthdayRepository;
    }

    @Override
    public Birthday save (Birthday birthday) throws ValidationException {
        LOGGER.info("Prepare to save new Birthday");
        LocalDateTime now = LocalDateTime.now();
        birthday.setCreated(now);
        birthday.setUpdated(now);

        try{
            validator.validateBirthday(birthday);
        }catch(InvalidEntityException e){
            throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e );
        }
        return birthdayRepository.save(birthday);
    }


}
