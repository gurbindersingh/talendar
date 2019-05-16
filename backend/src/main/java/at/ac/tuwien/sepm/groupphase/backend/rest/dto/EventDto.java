package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.EventType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "eventType",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BirthdayDto.class, name = "Birthday")
})
public abstract class EventDto {


    private Integer id;
    @NotNull
    private EventType eventType;
    private String name;
    private List<RoomUse> roomUses = new LinkedList<>();
    private LocalDateTime created;
    private LocalDateTime updated; //LocalDateTime > Date

    public EventDto(){

    }


    public EventDto (Integer id, EventType eventType, String name, List<RoomUse> roomUses, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.eventType = eventType;
        this.name = name;
        this.roomUses = roomUses;
        this.created = created;
        this.updated = updated;
    }


    public Integer getId () {
        return id;
    }


    public void setId (Integer id) {
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
