package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.IValidator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class TrainerService implements ITrainerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final IValidator<Trainer> validator;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, IValidator<Trainer> validator) {
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
            validator.validate(trainer);
        }
        catch(InvalidEntityException e) {
            LOGGER.error("validity violation: {}", e.getMessage(), e);
            throw new ValidationException("Given trainer instance is not valid: " + e.getMessage(), e);
        }

        try {
            return trainerRepository.save(trainer);
        } catch(Exception e) { //catch specific exceptions
            throw new ServiceException(e);
        }
    }
}
