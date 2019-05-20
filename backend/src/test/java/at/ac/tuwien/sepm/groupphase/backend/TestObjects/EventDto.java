package at.ac.tuwien.sepm.groupphase.backend.TestObjects;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class EventDto {


    private Long id;
    private EventType eventType;
    private String name;
    @JsonManagedReference
    private List<RoomUse> roomUses = new LinkedList<>();
    private LocalDateTime created;
    private LocalDateTime updated; //LocalDateTime > Date

    public EventDto(){

    }


    public EventDto (Long id, EventType eventType, String name, List<RoomUse> roomUses, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.eventType = eventType;
        this.name = name;
        this.roomUses = roomUses;
        this.created = created;
        this.updated = updated;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public EventType getEventType () {
        return eventType;
    }


    public void setEventType (EventType eventType) {
        this.eventType = eventType;
    }


    public String getName () {
        return name;
    }


    public void setName (String name) {
        this.name = name;
    }


    public List<RoomUse> getRoomUses () {
        return roomUses;
    }


    public void setRoomUses (List<RoomUse> roomUses) {
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
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EventDto eventDto = (EventDto) o;
        return Objects.equals(id, eventDto.id) &&
            eventType == eventDto.eventType &&
            Objects.equals(name, eventDto.name) &&
            Objects.equals(roomUses, eventDto.roomUses) &&
            Objects.equals(created, eventDto.created) &&
            Objects.equals(updated, eventDto.updated);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, eventType, name, roomUses, created, updated);
    }


    @Override
    public String toString () {
        return "EventDto{" +
            "id=" + id +
            ", eventType=" + eventType +
            ", name='" + name + '\'' +
            ", roomUses=" + roomUses +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
