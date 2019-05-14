package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface BirthdayRepository extends EventRepository<Birthday> {

}
