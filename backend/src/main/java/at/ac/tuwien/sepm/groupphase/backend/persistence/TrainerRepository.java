package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    List<Trainer> findByBirthdayTypes(String birthdayType);

    Optional<Trainer> findByIdAndDeletedFalse(Long id);

    List<Trainer> findAll();

    Trainer findByEvents_Id(Long id);
}
