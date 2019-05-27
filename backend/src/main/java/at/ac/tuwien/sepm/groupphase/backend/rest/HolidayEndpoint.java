package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.HolidayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Exception that occur within the underlying services will be reported and rethrown.
 *
 * All exception are annotated with @ResponseStatus(...)
 * Therefore Spring will automatically map such an thrown exception to an appropriate HTTP response
 * status (i.e. the status which was specified in annotation)
 */


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

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HolidayDto createNewHoliday(@RequestBody HolidayDto holidayDto) throws Exception {
        LOGGER.info("Incoming POST holiday Request");

        try {
            return mapper.entityToHolidayDto(holidayService.save(mapper.dtoToHolidayEntity(holidayDto)));
        }
        catch(Exception e) {
            LOGGER.error("POST Request Could Not Be Served Successfully - : {}", e.getMessage(), e);
            throw e;
        }
    }
}
