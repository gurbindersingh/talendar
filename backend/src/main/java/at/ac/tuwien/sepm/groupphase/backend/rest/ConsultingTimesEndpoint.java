package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimeDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimesDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IConsultingTimeService;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
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
@RequestMapping("/api/v1/talendar/consultingTimes")
public class ConsultingTimesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultingTimesEndpoint.class);


    private final IConsultingTimeService consultingTimeService;
    private final ConsultingTimeMapper mapper;

    @Autowired
    public ConsultingTimesEndpoint(IConsultingTimeService consultingTimeService, ConsultingTimeMapper mapper) {
        this.consultingTimeService = consultingTimeService;
        this.mapper = mapper;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public LinkedList<ConsultingTimeDto> createNewHolidays(@RequestBody
                                                               ConsultingTimesDto consultingTimesDto) throws Exception {
        LOGGER.info("Incoming POST consultingTimeSSSSSS Request");

        try {
            LinkedList<ConsultingTime> resultList = consultingTimeService.saveConsultingTimes(consultingTimesDto);
            LOGGER.info("Shoutl return to user:" + resultList);
            LinkedList<ConsultingTimeDto> r = mapper.entityListToConsultingTimeDtoList(resultList);
            LOGGER.info("Actually returning to user:" + r);

            LOGGER.info("Returning to user now r");
            return r;
        }
        catch(Exception e) {
            LOGGER.error("POST Request Could Not Be Served Successfully - : {}", e.getMessage(), e);
            throw e;
        }
    }
}
