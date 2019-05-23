package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TimeNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TrainerNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.HolidayRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.RoomUseRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import javax.sql.rowset.serial.SerialException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class EventService implements IEventService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RoomUseRepository roomUseRepository;
    private final Validator validator;
    private final TrainerRepository trainerRepository;
    private final HolidayRepository holidayRepository;

    @Autowired
    public EventService(EventRepository eventRepository, Validator validator, RoomUseRepository roomUseRepository, TrainerRepository trainerRepository, HolidayRepository holidayRepository){
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.roomUseRepository = roomUseRepository;
        this.trainerRepository = trainerRepository;
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Event save (Event event) throws ValidationException, ServiceException {
        LOGGER.info("Prepare to save new Event");
        LocalDateTime now = LocalDateTime.now();
        event.setCreated(now);
        event.setUpdated(now);

        try {
            Thread.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        switch(event.getEventType()) {
            case Birthday:
                  try {
                      validator.validateEvent(event);
                      event.setTrainer(findTrainerForBirthday(event.getRoomUses(), event.getBirthdayType()));
                      event = synchRoomUses(event);
                  }
                  catch(InvalidEntityException e) {
                      throw new ValidationException("Given Birthday is invalid: " + e.getMessage(), e);
                  }catch(TrainerNotAvailableException e){
                      throw new ValidationException("There are no Trainers available for this birthday " + e.getMessage(),e);
                  }

                  break;
            case Course:
                try {
                    validator.validateEvent(event);
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException("Given Course is invalid: " + e.getMessage(), e);
                }
                break;
            case Rent:
                try {
                    validator.validateEvent(event);
                }
                catch(InvalidEntityException e) {
                    throw new ValidationException("Given Rent is invalid: " + e.getMessage(), e);
                }
                break;
            case Consultation:
                try {
                    validator.validateEvent(event);
                    trainerAvailable(event.getTrainer(), event.getRoomUses());
                } catch (InvalidEntityException e) {
                    throw new ValidationException("Given Consultation is invalid: " + e.getMessage(), e);
                } catch (TrainerNotAvailableException e) {
                    throw new ValidationException("The specified trainer is not available during the allocated time frame" + e.getMessage(), e);
                }
                break;
        }
        try{
            isAvailable(event.getRoomUses());
        }catch(TimeNotAvailableException e){
            throw new ValidationException("The event is attempting to be booked during a different event: " + e.getMessage(), e);
        }
        return eventRepository.save(event);
    }

    public Trainer findTrainerForBirthday(List<RoomUse> roomUses, String birthdayType) throws TrainerNotAvailableException{
        List<Trainer> appropriateTrainers = trainerRepository.findByBirthdayTypes(birthdayType);
        Collections.shuffle(appropriateTrainers);
        for(Trainer t: appropriateTrainers
            ) {
            try {
                trainerAvailable(t, roomUses);
                return t;
            } catch (TrainerNotAvailableException e) {
                throw new TrainerNotAvailableException("There are no trainers who can do a " + birthdayType + " birthday during the allotted time");
            }
        }
        return null;
    }
    public Event synchRoomUses(Event event){
        for(RoomUse x: event.getRoomUses()
            ) {
            x.setEvent(event);
        }
        return event;
    }

    public void trainerAvailable(Trainer trainer, List<RoomUse> roomUses) throws TrainerNotAvailableException {
        List<RoomUse> trainersEvents = eventRepository.findByTrainer_IdAndRoomUses_BeginGreaterThanEqual(trainer.getId(), LocalDateTime.now());
        List<Holiday> trainerHoliday = holidayRepository.findByTrainer_Id(trainer.getId());

        for(RoomUse x: roomUses
            ) {
            for(RoomUse db: trainersEvents
                ) {
                if(x.getBegin().isAfter(db.getBegin()) && x.getBegin().isBefore(db.getEnd()) || x.getEnd().isBefore(db.getEnd()) && x.getEnd().isAfter(db.getBegin()) || x.getBegin().isBefore(db.getBegin()) && x.getEnd().isAfter(db.getEnd())) {
                    throw new TrainerNotAvailableException("The specified trainer is not available for the allocated time frame");
                }
            }
            for(Holiday db: trainerHoliday
                ) {
                if(x.getBegin().isAfter(db.getHolidayStart()) && x.getBegin().isBefore(db.getHolidayEnd()) || x.getEnd().isBefore(db.getHolidayEnd()) && x.getEnd().isAfter(db.getHolidayStart()) || x.getBegin().isBefore(db.getHolidayStart()) && x.getEnd().isAfter(db.getHolidayEnd())) {
                    throw new TrainerNotAvailableException("The specified trainer is not available for the allocated time frame");
                }
            }
        }
    }

    public void isAvailable(List<RoomUse> roomUseList) throws TimeNotAvailableException {
        LocalDateTime now = LocalDateTime.now();
        List<RoomUse> dbRooms = roomUseRepository.findByBeginGreaterThanEqual(now);
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
