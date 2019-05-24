package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<RoomUse> findByTrainer_IdAndRoomUses_BeginGreaterThanEqualAndDeletedIs(Long id, LocalDateTime now, boolean deleted);

    List<Event> findByEventTypeEqualsAndDeletedIs(EventType eventType, boolean deleted);

    @Query(value = "update Event e set e.deleted = true where e.id = ?1", nativeQuery = true)
    void deleteThisEvent(Long id);
}
