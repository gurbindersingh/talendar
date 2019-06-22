package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EventDto {


    /*
       These Variables are used by all event Types
    */
    private Long id;
    private EventType eventType;
    private String name;
    @JsonIgnoreProperties("event")
    private List<RoomUse> roomUses = new LinkedList<>();
    private LocalDateTime created;
    private LocalDateTime updated;
    @JsonIgnoreProperties("eventDtos")
    private List<CustomerDto> customerDtos = new LinkedList<>();
    private boolean deleted;

    /*
        These Variables are used by non Rent Types
     */
    @JsonIgnoreProperties({ "events", "holidays" })
    private Trainer trainer;

    /*
        These Variables are birthday specific
     */

    private int headcount;
    private int ageToBe;
    private String birthdayType;

     /*
        These Variables are Consulatation Specicfic
     */


    /*
        These Variables are Course Specific
     */

    private LocalDateTime endOfApplication;
    private Double price;
    private Integer maxParticipants;
    private String description;
    private Integer minAge;
    private Integer maxAge;
    private List<String> pictures;



    /*
        These Variables are Rent Specific
     */


    /*
        Signal whether this event comprises all informations
        or is is just marked as redacted (any its data are reset)
     */
    private boolean redacted;
    /*
        Signals that this event may is displayed in a differnet way (less prioritized) or is even
        hidden.
     */
    private boolean hide;


    public EventDto() {

    }


    public EventDto(Long id, EventType eventType, String name, List<RoomUse> roomUses,
                    LocalDateTime created, LocalDateTime updated, List<CustomerDto> customerDtos,
                    Trainer trainer, int headcount, int ageToBe, String birthdayType,
                    LocalDateTime endOfApplication, Double price, Integer maxParticipants,
                    String description, Integer minAge, Integer maxAge, List<String> pictures,
                    boolean deleted
    ) {
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
        this.endOfApplication = endOfApplication;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.pictures = pictures;
        this.deleted = deleted;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public EventType getEventType() {
        return eventType;
    }


    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<RoomUse> getRoomUses() {
        return roomUses;
    }


    public void setRoomUses(List<RoomUse> roomUses) {
        this.roomUses = roomUses;
    }


    public LocalDateTime getCreated() {
        return created;
    }


    public void setCreated(LocalDateTime created) {
        this.created = created;
    }


    public LocalDateTime getUpdated() {
        return updated;
    }


    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }


    public List<CustomerDto> getCustomerDtos() {
        return customerDtos;
    }


    public void setCustomerDtos(List<CustomerDto> customerDtos) {
        this.customerDtos = customerDtos;
    }


    public Trainer getTrainer() {
        return trainer;
    }


    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }


    public int getHeadcount() {
        return headcount;
    }


    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }


    public int getAgeToBe() {
        return ageToBe;
    }


    public void setAgeToBe(int ageToBe) {
        this.ageToBe = ageToBe;
    }


    public String getBirthdayType() {
        return birthdayType;
    }


    public void setBirthdayType(String birthdayType) {
        this.birthdayType = birthdayType;
    }


    public LocalDateTime getEndOfApplication() {
        return endOfApplication;
    }


    public void setEndOfApplication(LocalDateTime endOfApplication) {
        this.endOfApplication = endOfApplication;
    }


    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public Integer getMaxParticipants() {
        return maxParticipants;
    }


    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getMinAge() {
        return minAge;
    }


    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }


    public Integer getMaxAge() {
        return maxAge;
    }


    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }


    public List<String> getPictures() {
        return pictures;
    }


    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }


    public boolean isRedacted() {
        return redacted;
    }


    public void setRedacted(boolean redacted) {
        this.redacted = redacted;
    }


    public boolean isHide() {
        return hide;
    }


    public void setHide(boolean hide) {
        this.hide = hide;
    }


    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(Object o) {
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
               birthdayType == eventDto.birthdayType &&
               Objects.equals(endOfApplication, eventDto.endOfApplication) &&
               Objects.equals(price, eventDto.price) &&
               Objects.equals(maxParticipants, eventDto.maxParticipants) &&
               Objects.equals(description, eventDto.description) &&
               Objects.equals(minAge, eventDto.minAge) &&
               Objects.equals(maxAge, eventDto.maxAge) &&
               Objects.equals(pictures, eventDto.pictures);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, name, roomUses, created, updated, customerDtos, trainer,
                            headcount, ageToBe, birthdayType, endOfApplication, price,
                            maxParticipants, description, minAge, maxAge, pictures
        );
    }


    @Override
    public String toString() {
        return "EventDto{" +
               "id=" + id +
               ", eventType=" + eventType +
               ", name='" + name + '\'' +
               ", roomUses=" + roomUses +
               ", created=" + created +
               ", updated=" + updated +
               ", customers=" + customerDtos +
               ", trainer=" + trainer +
               ", headcount=" + headcount +
               ", ageToBe=" + ageToBe +
               ", birthdayType=" + birthdayType +
               ", endOfApplication=" + endOfApplication +
               ", price=" + price +
               ", maxParticipants=" + maxParticipants +
               ", description='" + description + '\'' +
               ", minAge=" + minAge +
               ", maxAge=" + maxAge +
               ", pictures=" + pictures +
               ", redacted=" + redacted +
               ", hide=" + hide +
               '}';
    }
}
