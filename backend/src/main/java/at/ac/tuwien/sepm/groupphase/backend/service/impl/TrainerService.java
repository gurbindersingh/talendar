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
}
