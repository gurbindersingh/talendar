package at.ac.tuwien.sepm.groupphase.backend.rest;


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


@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/api/v1/talendar/events")
public class EventEndpoint {
    private static Logger LOGGER = LoggerFactory.getLogger(TrainerEndpoint.class);

    private final IEventService eventService;
    private final EventMapper   eventMapper;


    @Autowired
    public EventEndpoint (IEventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @PostMapping
    public EventDto createNewEvent(@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming POST Request for an Event" + eventDto);
        try {
            return eventMapper.entityToEventDto(eventService.save(eventMapper.dtoToEventEntity(
                eventDto)));
        }
        catch(ValidationException e) {
            LOGGER.error("Error in the backend: " + e.getMessage(), e);
            throw new BackendException("Validation Error in the Backend: " + e.getMessage(), e);
        }
        catch(ServiceException e) {
            LOGGER.error("Error in the backend: " + e.getMessage(), e);
            throw new BackendException("Service Error in the Backend: " + e.getMessage(), e);
        }
    }
}
