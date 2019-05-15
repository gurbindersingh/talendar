package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// offers all crud opereations
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    // additional operations can be defined here
    // e.g.
    List<Template> findByTest(String toFind);



    // define search criteria within methodName

    // or use @Query annotation to define more complex queries on interface methods
}
