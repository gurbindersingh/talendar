package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialException;

@RestController
@RequestMapping("/api/talendar/events")
public class EventEndpoint {
    private  static Logger LOGGER = LoggerFactory.getLogger(TrainerEndpoint.class);

    private final IEventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public EventEndpoint(IEventService eventService, EventMapper eventMapper){
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @CrossOrigin("http://localhost:4200/")
    @PostMapping
    public EventDto createNewEvent(@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming POST Request for an Event with type: " + eventDto.getEventType());
        try {
            if(eventDto.getEventType() == EventType.Birthday) {
                return eventMapper.entityToEventDto(eventService.save(eventMapper.dtoToEventEntity((eventDto))));
            }
            else if(eventDto.getEventType() == EventType.Consultation) {
                //TODO: fill in the rest of the Event subtypes
            }
            else if(eventDto.getEventType() == EventType.Course) {
                return eventMapper.entityToEventDto(eventService.save(eventMapper.dtoToEventEntity(eventDto)));
            }
            else if(eventDto.getEventType() == EventType.Rent) {

            }
        }catch(ValidationException e){
            LOGGER.error("Error in the backend: " + e.getMessage(), e);
            throw new BackendException("Validation Error in the Backend: " + e.getMessage(), e);
        }catch(ServiceException e){
            LOGGER.error("Error in the backend: " + e.getMessage(), e);
            throw new BackendException("Service Error in the Backend: " + e.getMessage(), e);
        }
        return eventDto;
    }

}
