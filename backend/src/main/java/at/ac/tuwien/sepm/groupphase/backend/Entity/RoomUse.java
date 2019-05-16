package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.Room;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "room_use")
public class RoomUse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private LocalDateTime begin;
    @NotNull
    private LocalDateTime end;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;


    public RoomUse (Integer id, @NotNull LocalDateTime begin, @NotNull LocalDateTime end, @NotNull Room room, Event event) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.room = room;
        this.event = event;
    }


    public Integer getId () {
        return id;
    }


    public void setId (Integer id) {
        this.id = id;
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
        return Objects.equals(id, roomUse.id) &&
            Objects.equals(begin, roomUse.begin) &&
            Objects.equals(end, roomUse.end) &&
            room == roomUse.room &&
            Objects.equals(event, roomUse.event);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, begin, end, room, event);
    }


    @Override
    public String toString () {
        return "RoomUse{" +
            "id=" + id +
            ", begin=" + begin +
            ", end=" + end +
            ", room=" + room +
            ", event=" + event +
            '}';
    }
}
