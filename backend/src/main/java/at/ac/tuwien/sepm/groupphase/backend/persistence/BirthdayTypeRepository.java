package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.BirthdayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BirthdayTypeRepository extends JpaRepository<BirthdayType, Long> {

    Optional<BirthdayType> findByName(String name);

}
