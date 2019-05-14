package at.ac.tuwien.sepm.groupphase.backend.persistence.implementations;

import at.ac.tuwien.sepm.groupphase.backend.persistence.IEventDao;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Repository
public class EventDao implements IEventDao {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAService");
    private EntityManager entityManager = emf.createEntityManager();

    @Autowired
    public EventDao(){

    }
    @Override
    public Event insertBirthday(Birthday event){
        entityManager.persist(event);
        return event;
    }
}
