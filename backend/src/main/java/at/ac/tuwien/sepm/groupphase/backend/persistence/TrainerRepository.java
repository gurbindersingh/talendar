package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    List<Trainer> findByBirthdayTypes(String birthdayType);

    Optional<Trainer> findByIdAndDeletedFalse(Long id);

    List<Trainer> findAll();

    Trainer findByEvents_Id(Long id);

    @Transactional
    @Modifying
    @Query(value = "update Trainer t set t.deleted = true, t.birthday = :birthday, t.birthdayTypes = :birthdayTypes, t.email = :email, t.phone = :phone, t.updated = :updated where t.id = :id")
    void deleteThisTrainer(@Param("id") Long id,
                           @Param("birthday") LocalDate birthday,
                           @Param("birthdayTypes") List<String> birthdayTypes,
                           @Param("email") String email,
                           @Param("phone") String phone,
                           @Param("updated") LocalDateTime updated
    );
}
