package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.data.jpa.repository.Query;


public interface IEventService {
    Event save (Event event) throws ValidationException, ServiceException;

    void deleteEvent(Long id);

    void cancelEvent(Long id) throws ValidationException;
}
