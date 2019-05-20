package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventDto {


    /*
       These Variables are used by all event Types
    */
    private Long id;
    private EventType eventType;
    private String name;
    private List<RoomUse> roomUses = new LinkedList<>();
    private LocalDateTime created;
    private LocalDateTime updated;
    private Set<CustomerDto> customerDtos;

    /*
        These Variables are used by non Rent Types
     */

    private Trainer trainer;

    /*
        These Variables are birthday specific
     */

    private int headcount;
    private int ageToBe;
    private BirthdayType birthdayType;

     /*
        These Variables are Consulatation Specicfic
     */


    /*
        These Variables are Course Specific
     */

    /*
        These Variables are Rent Specific
     */

    public EventDto(){

    }


    public EventDto (Long id, EventType eventType, String name, List<RoomUse> roomUses, LocalDateTime created, LocalDateTime updated, Set<CustomerDto> customerDtos, Trainer trainer, int headcount, int ageToBe, BirthdayType birthdayType) {
        this.id = id;
        this.eventType = eventType;
        this.name = name;
        this.roomUses = roomUses;
        this.created = created;
        this.updated = updated;
        this.customerDtos = customerDtos;
        this.trainer = trainer;
        this.headcount = headcount;
        this.ageToBe = ageToBe;
        this.birthdayType = birthdayType;
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


    public Set<CustomerDto> getCustomerDtos () {
        return customerDtos;
    }


    public void setCustomerDtos (Set<CustomerDto> customerDtos) {
        this.customerDtos = customerDtos;
    }


    public Trainer getTrainer () {
        return trainer;
    }


    public void setTrainer (Trainer trainer) {
        this.trainer = trainer;
    }


    public int getHeadcount () {
        return headcount;
    }


    public void setHeadcount (int headcount) {
        this.headcount = headcount;
    }


    public int getAgeToBe () {
        return ageToBe;
    }


    public void setAgeToBe (int ageToBe) {
        this.ageToBe = ageToBe;
    }


    public BirthdayType getBirthdayType () {
        return birthdayType;
    }


    public void setBirthdayType (BirthdayType birthdayType) {
        this.birthdayType = birthdayType;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EventDto eventDto = (EventDto) o;
        return headcount == eventDto.headcount &&
            ageToBe == eventDto.ageToBe &&
            Objects.equals(id, eventDto.id) &&
            eventType == eventDto.eventType &&
            Objects.equals(name, eventDto.name) &&
            Objects.equals(roomUses, eventDto.roomUses) &&
            Objects.equals(created, eventDto.created) &&
            Objects.equals(updated, eventDto.updated) &&
            Objects.equals(customerDtos, eventDto.customerDtos) &&
            Objects.equals(trainer, eventDto.trainer) &&
            birthdayType == eventDto.birthdayType;
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, eventType, name, roomUses, created, updated, customerDtos, trainer, headcount, ageToBe, birthdayType);
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
            ", customerDtos=" + customerDtos +
            ", trainer=" + trainer +
            ", headcount=" + headcount +
            ", ageToBe=" + ageToBe +
            ", birthdayType=" + birthdayType +
            '}';
    }
}
