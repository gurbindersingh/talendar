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
        System.out.println("We are now here: Event save service\n");
        LOGGER.info("Prepare to save new Event");
        LocalDateTime now = LocalDateTime.now();
        event.setCreated(now);
        event.setUpdated(now);

        switch(event.getEventType()) {
            case Birthday:
                  try {
                      validator.validateBirthday(event);
                  }
                  catch(InvalidEntityException e) {
                      throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e);
                  }
                  break;

            case Course:
                try {
                    validator.validateCourse(event);
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException("Given Course is invalid: " + e.getMessage(), e);
                }
                break;

            case Rent:
                //TODO
                break;

            case Consultation:
                //TODO
                break;
        }
        return eventRepository.save(event);
    }


}
