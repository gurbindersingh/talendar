package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "room_use")
public class RoomUse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JsonIgnoreProperties("roomUses")
    private Event event;
    @Column
    private boolean deleted;

    public RoomUse(){

    }
    public RoomUse (Long id, @NotNull LocalDateTime begin, @NotNull LocalDateTime end, @NotNull Room room, Event event, boolean deleted) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.room = room;
        this.event = event;
        this.deleted = deleted;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
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


    public boolean isDeleted () {
        return deleted;
    }


    public void setDeleted (boolean deleted) {
        this.deleted = deleted;
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
        return Objects.hash(id, begin, end, room);
    }


    @Override
    public String toString () {
        return "RoomUse{" +
            "id=" + id +
            ", begin=" + begin +
            ", end=" + end +
            ", room=" + room +
            '}';
    }
}
