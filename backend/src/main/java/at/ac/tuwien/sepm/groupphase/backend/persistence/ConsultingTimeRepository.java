package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultingTimeRepository extends JpaRepository<ConsultingTime, Long> {

    List<ConsultingTime> findByTrainer_Id(Long id);
    List<ConsultingTime> findAll();
    void deleteByGroupId(Long groupId);
}
