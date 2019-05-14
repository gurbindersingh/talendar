package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.pojo.Event;

import javax.persistence.PersistenceException;

public interface IEventDao {
    /**
     * @param event is the Event to be inserted into the Database
     * @return the inserted Event
     * @throws PersistenceException when something goes wrong with databaase access
     */
    Event insertEvent(Event event) throws PersistenceException;
}
