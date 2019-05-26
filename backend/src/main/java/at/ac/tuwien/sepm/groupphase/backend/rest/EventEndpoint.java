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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createNewEvent (@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming POST Request for an Event with type: " + eventDto.toString());
        try {
            return eventMapper.entityToEventDto(eventService.save(eventMapper.dtoToEventEntity(
                eventDto)));
        }
        catch(ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }catch(ServiceException e){
            LOGGER.error(e.getMessage(), e);
        }
    }


    @GetMapping
    @RequestMapping("/trainer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getAllEvents (@PathVariable Long id) throws BackendException {
        /*
         * Pass the JWT to this method to authenticate the caller
         * and authorize the access to the corresponding resources.
         * */
        LOGGER.info("Incoming GET Request for all Events");
        try {
            return this.eventService.getAllEvents(id)
                                    .stream()
                                    .map(eventMapper::entityToEventDto)
                                    .collect(
                                        Collectors.toList());
            // return new ArrayList<EventDto>();
        }
        catch(ServiceException | ValidationException e) {
            throw new BackendException(e.getMessage(), e);
        }
    }
}
