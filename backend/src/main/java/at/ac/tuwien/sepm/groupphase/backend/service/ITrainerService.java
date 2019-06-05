package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;

import java.util.List;


public interface ITrainerService {

    /**
     * This method will save the given instance of trainer.
     *
     * @param trainer the given trainer to be saved
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    Trainer save (Trainer trainer, String password) throws ServiceException, ValidationException;

    /**
     * This method will update the trainer entity which is referenced by the given parameter (reference by ID:long)
     *
     * @param trainer the given trainer that holds the new values
     * @return the persistently saved and updated instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessfula operation.
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     * @throws NotFoundException will be thrown if no entity exists which is referenced by the id of the submitted instance.
     */
    Trainer update(Trainer trainer) throws ServiceException, ValidationException, NotFoundException;

    /**
     * This method returns an entity referenced by the given ID
     *
     * @param id the specific ID of a trainer
     * @return the saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data access that leads to an unsuccessful request.
     * @throws NotFoundException will be thrown if no entity exists with the given ID
     */
    Trainer getById(Long id) throws ServiceException, NotFoundException;

    /**
     * This method returns a list of all trainer instanced that are saved.
     *
     * @return  a List of all Trainers
     * @throws ServiceException will be thrown if any error occurs during data access that leads to an unsuccessful request.
     */
    List<Trainer> getAll() throws ServiceException;
}
