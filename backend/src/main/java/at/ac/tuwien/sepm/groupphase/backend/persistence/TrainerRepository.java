package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    List<Trainer> findByBirthdayTypes(String birthdayType);

    List<Trainer> findAllBy

    Trainer findByEvents_Id(Long id);

    @Transactional
    @Modifying
    @Query(value = "update Trainer t set t.deleted = true, t.birthday = :birthday where t.id = :id")
    void deleteThisEvent(@Param("id")Long id, @Param("birthday") LocalDate birthday);
}
