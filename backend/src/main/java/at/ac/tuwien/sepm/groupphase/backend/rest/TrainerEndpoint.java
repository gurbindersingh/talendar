package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.TrainerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception that occur within the underlying services will be reported and rethrown.
 *
 * All exception are annotated with @ResponseStatus(...)
 * Therefore Spring will automatically map such an thrown exception to an appropriate HTTP response
 * status (i.e. the status which was specified in annotation)
 */


@RestController
@RequestMapping("/api/v1/talendar/trainers")
public class TrainerEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerEndpoint.class);

    private final ITrainerService trainerService;
    private final TrainerMapper mapper;

    @Autowired
    public TrainerEndpoint(ITrainerService trainerService, TrainerMapper mapper) {
        this.trainerService = trainerService;
        this.mapper = mapper;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public TrainerDto getOneTrainerById(@PathVariable("id") Long id) throws BackendException {
        LOGGER.info("Incoming Request To Retrieve Trainer With ID {}", id);

        try {
            return mapper.entityToTrainerDto(trainerService.getById(id));
        } catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        } catch(NotFoundException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Der gesuchte Trainer existiert nicht", e);
        }
    }



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET)
    public List<TrainerDto> getAllTrainers() throws BackendException {
        LOGGER.info("Incoming Request To Retrieve List Of All Trainers");

        try {
            return trainerService.getAll().stream().map(mapper::entityToTrainerDto).collect(Collectors.toList());
        } catch(ServiceException e) {
            LOGGER.error("GET Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.PUT)
    public TrainerDto updateTrainer(@RequestBody TrainerDto trainerDto) throws BackendException {
        LOGGER.info("Incoming Request To Update An Existing Trainer With Id {}", trainerDto.getId());

        try {
            return mapper.entityToTrainerDto(trainerService.update(mapper.dtoToTrainerEntity(trainerDto)));
        } catch(ServiceException e) {
            LOGGER.error("PATCH Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        } catch(ValidationException e) {
            LOGGER.error("PATCH Request unsuccessful " + e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(NotFoundException e) {
            LOGGER.error("PATCH Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Es konnte nicht geupdated werden. Der Trainer existiert nicht", e);
        }
    }



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerDto createNewTrainer(@RequestBody TrainerDto trainerDto) throws BackendException {
        LOGGER.info("Incoming POST Trainer Request");

        try {
            return mapper.entityToTrainerDto(trainerService.save(mapper.dtoToTrainerEntity(trainerDto), trainerDto.getPassword()));
        } catch(ValidationException e) {
            LOGGER.error("POST Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        } catch(ServiceException e) {
            LOGGER.error("POST Request unsuccessful: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
    }
}
