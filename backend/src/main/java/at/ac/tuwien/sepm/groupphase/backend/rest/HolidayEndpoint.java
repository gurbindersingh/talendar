package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.HolidayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Exception that occur within the underlying services will be reported and rethrown.
 *
 * All exception are annotated with @ResponseStatus(...)
 * Therefore Spring will automatically map such an thrown exception to an appropriate HTTP response
 * status (i.e. the status which was specified in annotation)
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/talendar/holiday")
public class HolidayEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayEndpoint.class);

    private final IHolidayService holidayService;
    private final HolidayMapper mapper;

    @Autowired
    public HolidayEndpoint(IHolidayService holidayService, HolidayMapper mapper) {
        this.holidayService = holidayService;
        this.mapper = mapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HolidayDto createNewHoliday(@RequestBody HolidayDto holidayDto) throws Exception {
        LOGGER.info("Incoming POST holiday Request");
        LOGGER.info("DTO is {}", holidayDto);
        LOGGER.info("Entity is {}", mapper.dtoToHolidayEntity(holidayDto));

        try {
            return mapper.entityToHolidayDto(holidayService.save(mapper.dtoToHolidayEntity(holidayDto)));
        }
        catch(Exception e) {
            LOGGER.error("POST Request Could Not Be Served Successfully - : {}", e.getMessage(), e);
            throw e;
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public LinkedList<HolidayDto> getAllHolidaysByTrainerId(@PathVariable("id") Long id) throws
                                                                               BackendException {
        LOGGER.info("Incoming Request for all holidays by trainerid: {}", id);

        try {
            return mapper.entityListToHolidayDtoList(holidayService.getAllHolidaysByTrainerId(id));
        }
        catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
        catch(NotFoundException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Gesuchte Urlauube existieren nicht", e);
        }
    }


    @GetMapping(value = "/all/{id}")
    public LinkedList<HolidayDto> getAllHolidays(@PathVariable("id") Long id) throws BackendException {
        LOGGER.info("Incoming Request To Retrieve List Of All Trainers");

        try {
            return mapper.entityListToHolidayDtoList(holidayService.getAllHolidays(id));
        }
        catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
        catch(NotFoundException e) {
            LOGGER.error("Couldnt find any holidays" + e.getMessage());
            throw new BackendException("Es konnten keine Urlaube gefunden werden!", e);
        }
    }

}
