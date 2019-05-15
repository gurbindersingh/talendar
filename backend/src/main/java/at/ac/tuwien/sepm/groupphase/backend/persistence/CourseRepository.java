package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Course;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CourseRepository extends EventRepository<Course> {

}
