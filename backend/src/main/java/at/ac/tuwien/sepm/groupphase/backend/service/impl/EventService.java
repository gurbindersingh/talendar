package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDateTime;

@Service
public class EventService implements IEventService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final Validator validator;

    @Autowired
    public EventService(EventRepository eventRepository, Validator validator){
        this.eventRepository = eventRepository;
        this.validator = validator;
    }

    @Override
    public Event save (Event event) throws ValidationException {
        System.out.println("We are now here: Birthday save service\n");
        LOGGER.info("Prepare to save new Birthday");
        LocalDateTime now = LocalDateTime.now();
        event.setCreated(now);
        event.setUpdated(now);

        try{
            validator.validateBirthday(event);
        }catch(InvalidEntityException e){
            throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e );
        }
        return eventRepository.save(event);
    }


}
