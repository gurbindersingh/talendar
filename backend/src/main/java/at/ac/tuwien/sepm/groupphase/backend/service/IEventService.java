package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TrainerNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import java.util.List;


public interface IEventService {
    Event save (Event event) throws ValidationException, ServiceException, EmailException, NotFoundException;

    List<Event> getAllEvents(Long trainerId) throws ValidationException, ServiceException;

    List<Event> getAllEvents() throws ServiceException;

    /**
     * Prepare the given list of all events for clients by removing any sensitive data that should not
     * be publicly available.
     * @param events list of all events
     * @return the same list of events is returned but the internals of some events are reset
     */
    List<Event> getClientView(List<Event> events);

    /***
     * Mark all events that are not held by the given trainer as hidden and reset the name of the
     * events.
     * @param events list of all events
     * @param id the id of the trainer
     * @return a complete list of all events that are hosted by a trainer with the given id.
     *         all other events remain too but are marked as hidden/subordinate.
     */
    List<Event> getTrainerView(List<Event> events, Long id);

    /**
     *
     * @param event with the new customer to add. when customer id is null then a sign in is happening, otherwise a sign off
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation and if
     *                  customer list is null or empty
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     * @throws NotFoundException will be thrown if the given instance was not found
     */
    Event updateCustomers(Event event) throws ValidationException, NotFoundException, ServiceException;

    void deleteEvent(Long id);

    void cancelEvent(Long id) throws ValidationException;

    Event getEventById(Long id) throws ServiceException, NotFoundException;;

    List<Event> getAllFutureCourses();

    Event update(Event event) throws ValidationException, NotFoundException, ServiceException;
}
