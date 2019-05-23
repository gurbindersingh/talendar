package at.ac.tuwien.sepm.groupphase.backend.service.impl;

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
    public Trainer update (Trainer trainer) throws ServiceException, ValidationException {
        LOGGER.info("Prepare update for trainer with id {}.\nProposed updated entity: {}", trainer.getId(), trainer);
        LocalDateTime timeOfUpdate = LocalDateTime.now();

        Trainer currentVersion;
        trainer.setUpdated(timeOfUpdate);

        try {
            validator.validateTrainer(trainer);
        } catch(InvalidEntityException e) {
            throw new ValidationException("Given trainer is invalid: " + e.getMessage(), e);
        }

        // probably a sleep is needed

        try {
            currentVersion = trainerRepository.getOne(trainer.getId());
            // merge the new values from trainer (as received from frontend) to the stored entity (which is managed by JPA)
            return mergeTrainers(currentVersion, trainer);
        } catch(DataAccessException e) {
            throw new ServiceException("Error while performing merge and update operation", e);
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
