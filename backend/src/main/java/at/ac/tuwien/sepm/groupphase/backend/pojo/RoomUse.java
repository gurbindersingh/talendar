package at.ac.tuwien.sepm.groupphase.backend.pojo;

import at.ac.tuwien.sepm.groupphase.backend.pojo.enums.Room;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

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


    public RoomUse (@NotNull LocalDateTime begin, @NotNull LocalDateTime end, @NotNull Room room, Event event) {
        this.begin = begin;
        this.end = end;
        this.room = room;
        this.event = event;
    }


    public LocalDateTime getBegin () {
        return begin;
    }


    public void setBegin (LocalDateTime begin) {
        this.begin = begin;
    }


    public LocalDateTime getEnd () {
        return end;
    }


    public void setEnd (LocalDateTime end) {
        this.end = end;
    }


    public Room getRoom () {
        return room;
    }


    public void setRoom (Room room) {
        this.room = room;
    }


    public Event getEvent () {
        return event;
    }


    public void setEvent (Event event) {
        this.event = event;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        RoomUse roomUse = (RoomUse) o;
        return Objects.equals(begin, roomUse.begin) &&
            Objects.equals(end, roomUse.end) &&
            room == roomUse.room &&
            Objects.equals(event, roomUse.event);
    }


    @Override
    public int hashCode () {
        return Objects.hash(begin, end, room, event);
    }


    @Override
    public String toString () {
        return "RoomUse{" +
            "begin=" + begin +
            ", end=" + end +
            ", room=" + room +
            ", event=" + event +
            '}';
    }
}
