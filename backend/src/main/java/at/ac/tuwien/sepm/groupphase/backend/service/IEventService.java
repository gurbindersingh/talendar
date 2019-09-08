package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.TrainerNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;

import java.util.List;


public interface IEventService {


    /**
     * @param event to save
     *
     * @return the saved event with id
     *
     * @throws ValidationException if event is invalid
     * @throws ServiceException    if internal error
     * @throws EmailException      if sending email caused error
     * @throws NotFoundException   if event is not found after saving which is impossible, but still to check
     */
    Event save(Event event) throws ValidationException, ServiceException, EmailException,
                                   NotFoundException;

    /**
     * get all events from trainer with id
     *
     * @param trainerId to filter events
     *
     * @return list of all events of trainer with id
     *
     * @throws ValidationException if id is invalid
     * @throws ServiceException    if internal error
     */
    List<Event> getAllEvents(Long trainerId) throws ValidationException, ServiceException;

    /**
     * get all events
     *
     * @return list of all events
     *
     * @throws ServiceException if internal error
     */
    List<Event> getAllEvents() throws ServiceException;

    /**
     * Prepare the given list of all events for clients by removing any sensitive data that should not
     * be publicly available.
     *
     * @param events list of all events
     *
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

    /***
     * Get events of trainer with id and set rent and events not belonging to trainer to hidden
     * @param id of trainer
     * @param events of the trainer with id
     * @return get all events of trainer with id
     *
     */
    List<Event> getTrainerView(List<Event> events, Long id);

    /**
     * @param events
     * @param id
     *
     * @return
     */
    List<Event> getAdminView(List<Event> events, Long id);

    /***
     * Get all events of trainer with id
     * @param id of trainer
     * @param events  of the trainer with id
     * @return get all events of trainer with id
     *
     */
    List<Event> filterTrainerEvents(List<Event> events, Long id);

    /**
     * @param event with the new customer to add. when customer id is null then a sign in is happening, otherwise a sign off
     *
     * @return the persistently saved instance is returned
     *
     * @throws ServiceException    will be thrown if any error occurs during data processing that leads to an unsuccessful operation and if
     *                             customer list is null or empty
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     * @throws NotFoundException   will be thrown if the given instance was not found
     */
    Event updateCustomers(Event event) throws ValidationException, NotFoundException,
                                              ServiceException;


    /***
     * delete event with id
     * @param id of event to delete
     *
     */
    void deleteEvent(Long id);

    /***
     * cancel event with id
     * @param id of event to cancel
     * @throws ValidationException if event is invalid to cancel
     *
     */
    void cancelEvent(Long id) throws ValidationException;


    /***
     * get event by id
     * @param id of event
     * @return get all events of trainer with id
     * @throws NotFoundException if event is not found
     * @throws ServiceException if internal error
     *
     */
    Event getEventById(Long id) throws ServiceException, NotFoundException;
    ;

    /***
     * @return all courses which are in future
     */
    List<Event> getAllFutureCourses();

    /***
     * @return all events which are in future
     * @throws ServiceException if internal error
     */
    List<Event> getAllFutureEvents() throws ServiceException;


    /***
     * update event
     * @return all events which are in future
     * @throws ServiceException if internal error
     * @throws ValidationException if event is invalid
     * @throws NotFoundException if event is not found
     */
    Event update(Event event) throws ValidationException, NotFoundException, ServiceException;
}
