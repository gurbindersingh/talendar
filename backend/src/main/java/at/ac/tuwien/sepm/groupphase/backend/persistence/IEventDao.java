package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.pojo.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Event;

import javax.persistence.PersistenceException;

public interface IEventDao {
    /**
     * @param bday is the birthday to be inserted into the Database
     * @return the inserted Event
     */
    Event insertBirthday(Birthday bday);
}
