package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.UserAccountConfigurationProperties;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService implements ITrainerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final EventService eventService;
    private final Validator validator;
    private final InfoMail infoMail;

    private final PasswordEncoder passwordEncoder;
    private final UserAccountConfigurationProperties userAccountConfigurationProperties;


    @Autowired
    public TrainerService(TrainerRepository trainerRepository, Validator validator,
                          EventService eventService, InfoMail infoMail, PasswordEncoder passwordEncoder,
                          UserAccountConfigurationProperties userAccountConfigurationProperties
    ) {
        this.trainerRepository = trainerRepository;
        this.eventService = eventService;
        this.validator = validator;
        this.infoMail = infoMail;
        this.passwordEncoder = passwordEncoder;
        this.userAccountConfigurationProperties = userAccountConfigurationProperties;
    }


    @Transactional
    @Override
    public Trainer save(Trainer trainer) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new trainer: {}", trainer);
        LocalDateTime timeOfCreation = LocalDateTime.now().minusSeconds(1);

        trainer.setCreated(timeOfCreation);
        trainer.setUpdated(timeOfCreation);

        try {
            validator.validateTrainer(trainer);
        }
        catch(InvalidEntityException e) {
            throw new ValidationException(e.getMessage(), e);
        }

        trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));

        try {
            trainer = trainerRepository.save(trainer);
            try {
                infoMail.sendAdminTrainerInfoMail(trainer, "Neuer Trainer erstellt", "newTrainer");
            }
            catch(EmailException e) {
                LOGGER.error("Error trying to send new trainer info mail to admin");
            }
        }
        catch(DataAccessException e) { //catch specific exceptions
            throw new ServiceException("Error while performing a data access operation", e);
        }

        return trainer;
    }


    @Transactional
    @Override
    public Trainer update(Trainer trainer) throws ServiceException, ValidationException,
                                                  NotFoundException {
        LOGGER.info("Prepare update for trainer with id {}.\nProposed updated entity: {}",
                    trainer.getId(), trainer
        );
        LocalDateTime timeOfUpdate = LocalDateTime.now();

        Optional<Trainer> queryResult;
        Trainer currentVersion;
        trainer.setUpdated(timeOfUpdate);

        try {
            validator.validateTrainer(trainer);
        }
        catch(InvalidEntityException e) {
            throw new ValidationException(e.getMessage(), e);
        }

        // probably a sleep is needed

        try {
            queryResult = trainerRepository.findById(trainer.getId());

            // retrieve query result
            if(queryResult.isPresent()) {
                currentVersion = queryResult.get();
            } else {
                // if not found, no update is possible
                throw new NotFoundException(
                    "Trainer with id " + trainer.getId() + " does not exist. Update not possible");
            }

            // merge the new values from trainer (as received from frontend) to the stored entity (which is managed by JPA)
            return mergeTrainers(currentVersion, trainer);
        }
        catch(DataAccessException e) {
            throw new ServiceException("Error while performing merge and update operation", e);
        }
    }


    @Override
    public void delete(Long id) throws ServiceException, NotFoundException {
        LOGGER.info("Try to delete trainer with id {}", id);
        LocalDateTime timeOfUpdate = LocalDateTime.now();

        Optional<Trainer> queryResult;
        Trainer currentVersion;

        try {
            queryResult = trainerRepository.findByIdAndDeletedFalse(id);

            if(queryResult.isPresent()) {
                currentVersion = queryResult.get();
            } else {
                throw new NotFoundException(
                    "Trainer with id " + id + " does not exist. Delete not possible");
            }

            currentVersion.setBirthday(LocalDate.MIN);
            //            currentVersion.setBirthdayTypes(null);
            currentVersion.setEmail("example@example.com");
            currentVersion.setPhone("06641234567");
            currentVersion.setUpdated(timeOfUpdate);

            if(!( currentVersion.getEvents() == null || currentVersion.getEvents().isEmpty() )) {
                for(Event e : currentVersion.getEvents()) {
                    eventService.deleteEvent(e.getId());
                }
            }
            trainerRepository.deleteThisTrainer(currentVersion.getId(), timeOfUpdate);
            try {
                infoMail.sendAdminTrainerInfoMail(currentVersion, "Trainer gelöscht",
                                                  "deleteTrainer"
                );
            }
            catch(EmailException e) {
                LOGGER.error("Error trying to send delete trainer info mail to admin");
            }
        }
        catch(DataAccessException e) {
            throw new ServiceException(
                "Error while setting new values and persisting the deleted trainer", e);
        }
    }


    @Override
    public Trainer getById(Long id) throws ServiceException, NotFoundException {
        LOGGER.info("Try to retrieve trainer with id {}", id);

        Optional<Trainer> result;

        try {
            result = trainerRepository.findByIdAndDeletedFalse(id);
        }
        catch(DataAccessException e) {
            throw new ServiceException("Error while performing a data access operation", e);
        }

        if(result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("The trainer with id " + id + " does not exist");
        }
    }


    @Override
    public List<Trainer> getAll() throws ServiceException {
        LOGGER.info("Try to retrieve a list of all trainers");

        try {
            return trainerRepository.findAll();
        }
        catch(DataAccessException e) {
            throw new ServiceException("Error while performing a data access operation", e);
        }
    }


    /**
     * This Will surprisingly persist the changes.
     *
     * NOTE: JPA will keep track of each entity that has been retrieved.
     * If this entities' values are reset, this changes are reflected to the database
     *
     * @param persisted  the JPA tracked entity
     * @param newVersion the new version
     */
    private Trainer mergeTrainers(Trainer persisted, Trainer newVersion) {
        // all values can be updated except id (obviously), timestamps, and related events
        persisted.setFirstName(newVersion.getFirstName());
        persisted.setLastName(newVersion.getLastName());
        persisted.setBirthday(newVersion.getBirthday());
        persisted.setBirthdayTypes(newVersion.getBirthdayTypes());
        persisted.setEmail(newVersion.getEmail());
        persisted.setPassword(newVersion.getPassword());
        persisted.setPhone(newVersion.getPhone());
        persisted.setUpdated(newVersion.getUpdated());
        return persisted;
    }
}
