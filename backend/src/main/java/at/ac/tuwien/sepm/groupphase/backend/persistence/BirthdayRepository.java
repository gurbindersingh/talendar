package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface BirthdayRepository extends EventRepository<Birthday> {

}
