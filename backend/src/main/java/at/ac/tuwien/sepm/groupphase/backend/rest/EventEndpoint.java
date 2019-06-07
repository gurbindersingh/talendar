package at.ac.tuwien.sepm.groupphase.backend.rest;


import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.RoomUseRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.RoomUseDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.RoomUseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/api/v1/talendar/events")
public class EventEndpoint {
    private static Logger LOGGER = LoggerFactory.getLogger(EventEndpoint.class);

    private final IEventService eventService;
    private final EventMapper   eventMapper;


    @Autowired
    public EventEndpoint (IEventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createNewEvent (@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming POST Request for an Event with type: " + eventDto.toString());
        try {
            return eventMapper.entityToEventDto(eventService.save(eventMapper.dtoToEventEntity(eventDto)));
        }catch(ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }catch(ServiceException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEvent(@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming PUT Request for an Event with type: " + eventDto.toString());
        try {
            return eventMapper.entityToEventDto(eventService.update(eventMapper.dtoToEventEntity(eventDto)));
        } catch(ValidationException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(ServiceException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/customers", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEventCustomers(@RequestBody EventDto eventDto) throws BackendException {
        LOGGER.info("Incoming PUT Request (update customers) for an Event with type: " + eventDto.toString());
        try {
            return eventMapper.entityToEventDto(eventService.updateCustomers(eventMapper.dtoToEventEntity(eventDto)));
        } catch(ValidationException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(ServiceException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void cancelEvent(@PathVariable("id") Long id) throws BackendException{
        LOGGER.info("Incoming DELETE Request for an Event with id " + id);
        try {
            eventService.cancelEvent(id);
        }catch(ValidationException e){
            throw new BackendException(e.getMessage(), e);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EventDto getEventById(@PathVariable("id") Long id) throws BackendException{
        LOGGER.info("Incoming GET Request for an Event with id " + id);
        try {
            return eventMapper.entityToEventDto(eventService.getEventById(id));
        } catch(NotFoundException ne) {
            throw new BackendException(ne.getMessage(), ne);
        } catch(ServiceException se) {
            throw new BackendException(se.getMessage(), se);
        }
    }

    @GetMapping(value = "/all")
    public List<EventDto> getAllEvents() throws BackendException {
        LOGGER.info("Incoming GET Request to retrieve all events");

        try {
            return this.eventService.getAllEvents().stream().map(eventMapper::entityToEventDto).collect(
                Collectors.toList());
        }
        catch(ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Es konnten keine Events geladen werden, etwas ist im Server schiefgelaufen", e);
        }
    }
    /*
    @GetMapping
    @RequestMapping(value = "/trainer/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getAllEvents (@PathVariable Long id) throws BackendException {
        /*
         * Pass the JWT to this method to authenticate the caller
         * and authorize the access to the corresponding resources.
         * *//*
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
    */

    @GetMapping
    public List<EventDto> getAllFutureCourses(){
        LOGGER.info("Incoming GET Request for all future Courses");
        return eventMapper.entityListToEventDtoList(eventService.getAllFutureCourses());
    }



}
