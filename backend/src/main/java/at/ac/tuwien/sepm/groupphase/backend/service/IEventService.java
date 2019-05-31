package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TrainerNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import java.util.List;


public interface IEventService {
    Event save (Event event) throws ValidationException, ServiceException;

    List<Event> getAllEvents(Long trainerId) throws ValidationException, ServiceException;

    List<Event> getAllEvents() throws ServiceException;


    /**
     * (!!!the whole customer list will be replaced!!!)
     *
     * @param event the given event with the new customer list to replace
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation and if
     *                  customer list is null or empty
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     * @throws NotFoundException will be thrown if the given instance was not found
     */
    Event updateCustomers(Event event) throws ValidationException, NotFoundException, ServiceException;

    void deleteEvent(Long id);

    void cancelEvent(Long id) throws ValidationException;

    Event getEventById(Long id);

    List<Event> getAllFutureCourses();

    Event update(Event event) throws ValidationException, NotFoundException, ServiceException;
}
