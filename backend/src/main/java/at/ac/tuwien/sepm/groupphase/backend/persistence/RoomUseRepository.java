package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomUseRepository extends JpaRepository<RoomUse, Long> {

    List<RoomUse> findByBeginGreaterThanEqual(LocalDateTime begin);
}
