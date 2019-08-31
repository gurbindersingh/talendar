package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimeDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IConsultingTimeService;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.ConsultingTimeMapper;
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
@RequestMapping("/api/v1/talendar/consultingTime")
public class ConsultingTimeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultingTimeEndpoint.class);

    private final IConsultingTimeService consultingTimeService;
    private final ConsultingTimeMapper mapper;

    @Autowired
    public ConsultingTimeEndpoint(IConsultingTimeService consultingTimeService, ConsultingTimeMapper mapper) {
        this.consultingTimeService = consultingTimeService;
        this.mapper = mapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultingTimeDto createNewConsultingTime(@RequestBody ConsultingTimeDto consultingTimeDto) throws Exception {
        LOGGER.info("Incoming POST consultingTime Request");
        LOGGER.info("DTO is {}", consultingTimeDto);
        LOGGER.info("Entity is {}", mapper.dtoToConsultingTimeEntity(consultingTimeDto));

        try {
            return mapper.entityToConsultingTimeDto(consultingTimeService.save(mapper.dtoToConsultingTimeEntity(consultingTimeDto)));
        }
        catch(Exception e) {
            LOGGER.error("POST Request Could Not Be Served Successfully - : {}", e.getMessage(), e);
            throw e;
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public LinkedList<ConsultingTimeDto> getAllConsultingTimesByTrainerId(@PathVariable("id") Long id) throws
                                                                               BackendException {
        LOGGER.info("Incoming Request for all consulting Times by trainerid: {}", id);

        try {
            return mapper.entityListToConsultingTimeDtoList(consultingTimeService.getAllConsultingTimesByTrainerId(id));
        }
        catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
        catch(NotFoundException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Gesuchte Beratungszeiten existieren nicht", e);
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public LinkedList<ConsultingTimeDto> getAllConsultingTimes() throws BackendException {
        LOGGER.info("Incoming Request To Retrieve List Of All Consulting times");

        try {
            return mapper.entityListToConsultingTimeDtoList(consultingTimeService.getAllConsultingTimes());
        }
        catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
        catch(NotFoundException e) {
            LOGGER.error("Couldnt find any holidays" + e.getMessage());
            throw new BackendException("Es konnten keine Beratungszeiten gefunden werden!", e);
        }
    }

}
