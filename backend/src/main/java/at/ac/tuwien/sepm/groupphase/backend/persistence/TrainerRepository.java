package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends UserBaseRepository<Trainer> {

    List<Trainer> findByBirthdayTypes(String birthdayType);

    @Transactional
    @Modifying
    @Query(value = "update Trainer t set t.deleted = true, t.updated = :updated where t.id = :id")
    void deleteThisTrainer(@Param("id") Long id, @Param("updated") LocalDateTime updated);

    Trainer findByEvents_Id(Long id);

}
