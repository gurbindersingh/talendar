package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;

@Entity
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "occupies_rooms")
    private LinkedList<RoomUse> roomUses;
    @Column(name = "created", nullable = false, updatable = false)
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created;
    @Column(name = "updated", nullable = false)
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updated;

    public Event(){

    }
    public Event (Long id, @NotBlank String name, @NotNull LinkedList<RoomUse> roomUses, @NotNull @Past LocalDateTime created, @NotNull @Past LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.roomUses = roomUses;
        this.created = created;
        this.updated = updated;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
            Objects.equals(name, event.name) &&
            Objects.equals(roomUses, event.roomUses) &&
            Objects.equals(created, event.created) &&
            Objects.equals(updated, event.updated);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, name, roomUses, created, updated);
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public String getName () {
        return name;
    }


    public void setName (String name) {
        this.name = name;
    }


    public LinkedList<RoomUse> getRoomUses () {
        return roomUses;
    }


    public void setRoomUses (LinkedList<RoomUse> roomUses) {
        this.roomUses = roomUses;
    }


    public LocalDateTime getCreated () {
        return created;
    }


    public void setCreated (LocalDateTime created) {
        this.created = created;
    }


    public LocalDateTime getUpdated () {
        return updated;
    }


    public void setUpdated (LocalDateTime updated) {
        this.updated = updated;
    }


    @Override
    public String toString () {
        return "Event{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", roomUses=" + roomUses +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
