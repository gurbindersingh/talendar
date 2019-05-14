package at.ac.tuwien.sepm.groupphase.backend.pojo;

import at.ac.tuwien.sepm.groupphase.backend.pojo.enums.Room;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RoomUse {
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime begin;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
