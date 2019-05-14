package at.ac.tuwien.sepm.groupphase.backend.persistence.implementations;

import at.ac.tuwien.sepm.groupphase.backend.persistence.IEventDao;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Event;

import javax.persistence.EntityManager;

public class EventDao implements IEventDao {
    private EntityManager entityManager;

    public Event insertBirthday(Birthday event){
        entityManager.persist(event);
        return event;
    }
}
