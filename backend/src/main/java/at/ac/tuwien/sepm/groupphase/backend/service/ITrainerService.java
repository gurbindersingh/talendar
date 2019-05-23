package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;


public interface ITrainerService {

    /**
     * This method will save the given instance of trainer.
     *
     * @param trainer the given trainer to be saved
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    Trainer save (Trainer trainer) throws ServiceException, ValidationException;

    /**
     * This method will update the trainer entity which is referenced by the given parameter (reference by ID:long)
     *
     * @param trainer the given trainer that holds the new values
     * @return the persistently saved and updated instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessfula operation.
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    Trainer update(Trainer trainer) throws ServiceException, ValidationException;
}
