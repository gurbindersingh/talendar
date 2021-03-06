package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomUseRepository extends JpaRepository<RoomUse, Long> {

    List<RoomUse> findByBeginGreaterThanEqualAndDeletedFalse(LocalDateTime begin);

    List<RoomUse> findByEvent_IdAndEvent_DeletedFalse(Long id);

    List<RoomUse> findByEvent_Trainer_IdAndBeginGreaterThanEqualAndEvent_DeletedFalse(Long id, LocalDateTime now);
}
