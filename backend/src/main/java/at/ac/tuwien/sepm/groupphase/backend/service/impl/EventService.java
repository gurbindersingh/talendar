package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TimeNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.RoomUseRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService implements IEventService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RoomUseRepository roomUseRepository;
    private final Validator validator;

    @Autowired
    public EventService(EventRepository eventRepository, Validator validator, RoomUseRepository roomUseRepository){
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.roomUseRepository = roomUseRepository;
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
                      isAvailable(event.getRoomUses());
                  }
                  catch(InvalidEntityException e) {
                      throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e);
                  }catch(TimeNotAvailableException e){
                      throw new ValidationException("Birthday is being booked during another event: " + e.getMessage(), e);
                  }

                  break;

            case Course:
                try {
                    validator.validateCourse(event);
                    isAvailable(event.getRoomUses());
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException("Given Course is invalid: " + e.getMessage(), e);
                }catch(TimeNotAvailableException e){
                    throw new ValidationException("Given Course is being booked during another event: " + e.getMessage(), e);
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

    public void isAvailable(List<RoomUse> roomUseList) throws TimeNotAvailableException {
        LocalDateTime now = LocalDateTime.now();
        List<RoomUse> dbRooms = roomUseRepository.findbyBeginGreaterThanEqual(now);
        for(RoomUse x: roomUseList
            ) {
            for(RoomUse db: roomUseList
                ) {
                if(x.getRoom() == db.getRoom()) {
                    if(x.getBegin().isAfter(db.getBegin()) && x.getBegin().isBefore(db.getEnd()) || x.getEnd().isBefore(db.getEnd()) && x.getEnd().isAfter(db.getBegin()) || x.getBegin().isBefore(db.getBegin()) && x.getEnd().isAfter(db.getEnd())) {
                        throw new TimeNotAvailableException("The Timeslot attempting to be booked is invalid. Attempted Booking: " + x.toString());
                    }
                }
            }
        }
    }
}
