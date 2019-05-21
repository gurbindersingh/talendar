package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;



public interface IEventService {
    Event save (Event event) throws ValidationException;
}
