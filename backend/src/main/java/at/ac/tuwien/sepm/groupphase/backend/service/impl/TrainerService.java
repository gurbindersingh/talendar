package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.IValidator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TrainerService implements ITrainerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final Validator validator;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, Validator validator) {
        this.trainerRepository = trainerRepository;
        this.validator = validator;
    }

    @Override
    public Trainer save (Trainer trainer) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new trainer: {}", trainer);
        LocalDateTime timeOfCreation = LocalDateTime.now();

        trainer.setCreated(timeOfCreation);
        trainer.setUpdated(timeOfCreation);
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        }catch(InterruptedException e){
            throw new ServiceException("Internal Server error", e);
        }
        try {
            Thread.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        try {
            validator.validateTrainer(trainer);
        } catch(InvalidEntityException e) {
            throw new ValidationException("Given trainer is invalid: " + e.getMessage(), e);
        }

        try {
            return trainerRepository.save(trainer);
        } catch(DataAccessException e) { //catch specific exceptions
            throw new ServiceException("Error while performing a data access operation", e);
        }
    }


    @Override
    public Trainer update (Trainer trainer) throws ServiceException, ValidationException, NotFoundException {
        LOGGER.info("Prepare update for trainer with id {}.\nProposed updated entity: {}", trainer.getId(), trainer);
        LocalDateTime timeOfUpdate = LocalDateTime.now();

        Optional<Trainer> queryResult;
        Trainer currentVersion;
        trainer.setUpdated(timeOfUpdate);

        try {
            validator.validateTrainer(trainer);
        } catch(InvalidEntityException e) {
            throw new ValidationException("Given trainer is invalid: " + e.getMessage(), e);
        }

        // probably a sleep is needed

        try {
            queryResult = trainerRepository.findById(trainer.getId());

            // retrieve query result
            if (queryResult.isPresent()) {
                currentVersion = queryResult.get();
            } else {
                // if not found, no update is possible
                throw new NotFoundException("Trainer with id " + trainer.getId() + " does not exist. Update not possible");
            }

            // merge the new values from trainer (as received from frontend) to the stored entity (which is managed by JPA)
            return mergeTrainers(currentVersion, trainer);
        } catch(DataAccessException e) {
            throw new ServiceException("Error while performing merge and update operation", e);
        }
    }


    @Override
    public Trainer getById (Long id) throws ServiceException, NotFoundException {
        LOGGER.info("Try to retrieve trainer with id {}", id);

        Optional<Trainer> result;

        try {
            result = trainerRepository.findById(id);
        } catch(DataAccessException e) {
            throw new ServiceException("Error while performing a data access operation", e);
        }

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("The trainer with id " + id + " does not exist");
        }
    }


    @Override
    public List<Trainer> getAll () throws ServiceException {
        LOGGER.info("Try to retrieve a list of all trainers");

        try {
            return trainerRepository.findAll();
        } catch(DataAccessException e) {
            throw new ServiceException("Error while performing a data access operation", e);
        }
    }


    /**
     *  This Will surprisingly persist the changes.
     *
     *  NOTE: JPA will keep track of each entity that has been retrieved.
     *  If this entities' values are reset, this changes are reflected to the database
     *
     * @param persisted the JPA tracked entity
     * @param newVersion the new version
     */
    private Trainer mergeTrainers(Trainer persisted, Trainer newVersion) {
        // all values can be updated except id (obviously), timestamps, and related events
        persisted.setFirstName(newVersion.getFirstName());
        persisted.setLastName(newVersion.getLastName());
        persisted.setBirthday(newVersion.getBirthday());
        persisted.setBirthdayTypes(newVersion.getBirthdayTypes());
        persisted.setEmail(newVersion.getEmail());
        persisted.setPhone(newVersion.getPhone());
        return persisted;
    }
}
