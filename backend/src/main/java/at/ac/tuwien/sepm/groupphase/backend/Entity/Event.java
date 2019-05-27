package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
public class Event {

    /*
        These Variables are used by all event Types
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY,
               mappedBy = "event",
               orphanRemoval = true,
               cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    //check how cascade works in all methods
    @JsonIgnoreProperties("event")
    private List<RoomUse> roomUses = new LinkedList<>();

    @Column(name = "created", updatable = false)
    @Past
    @NotNull
    private LocalDateTime created;

    @Column(name = "updated")
    @Past
    @NotNull
    private LocalDateTime updated; //LocalDateTime > Date

    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
        name = "event_customer",
        joinColumns = { @JoinColumn(name = "fk_event", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_customer" , referencedColumnName = "id")}
    )
    @JsonIgnoreProperties("events")
    private Set<Customer> customers;

    @Column
    private boolean deleted;

    /*
        These Variables are used by non Rent Types
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("event")
    private Trainer trainer;


    /*
        These Variables are birthday specific
     */

    @Column
    private int headcount;
    @Column
    private int ageToBe;
    @Column
    private String birthdayType;

    /*
        These Variables are Consulatation Specicfic
     */


    /*
        These Variables are Course Specific
     */

    @Column
    private LocalDateTime endOfApplication;

    @Column
    private Double price;

    @Column
    private Integer maxParticipants;

    @Column
    private String description;

    @Column
    private Integer minAge;


    @Column
    private Integer maxAge;


    /*
        These Variables are Rent Specific
     */


    public Event () {

    }


    public Event (@NotBlank String name, @NotNull List<RoomUse> roomUses, @Past @NotNull LocalDateTime created, @Past @NotNull LocalDateTime updated, EventType eventType, Set<Customer> customers, Trainer trainer, int headcount, int ageToBe, String birthdayType, LocalDateTime endOfApplication, Double price, Integer maxParticipants, String description, Integer minAge, Integer maxAge, boolean deleted) {
        this.name = name;
        this.roomUses = roomUses;
        this.created = created;
        this.updated = updated;
        this.eventType = eventType;
        this.customers = customers;
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
        this.deleted = deleted;
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


    public EventType getEventType () {
        return eventType;
    }


    public void setEventType (EventType eventType) {
        this.eventType = eventType;
    }


    public Set<Customer> getCustomers () {
        return customers;
    }


    public void setCustomers (Set<Customer> customers) {
        this.customers = customers;
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


    public String getBirthdayType () {
        return birthdayType;
    }


    public void setBirthdayType (String birthdayType) {
        this.birthdayType = birthdayType;
    }


    public LocalDateTime getEndOfApplication () {
        return endOfApplication;
    }


    public void setEndOfApplication (LocalDateTime endOfApplication) {
        this.endOfApplication = endOfApplication;
    }


    public Double getPrice () {
        return price;
    }


    public void setPrice (Double price) {
        this.price = price;
    }


    public Integer getMaxParticipants () {
        return maxParticipants;
    }


    public void setMaxParticipants (Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public String getDescription () {
        return description;
    }


    public void setDescription (String description) {
        this.description = description;
    }


    public Integer getMinAge () {
        return minAge;
    }


    public void setMinAge (Integer minAge) {
        this.minAge = minAge;
    }


    public Integer getMaxAge () {
        return maxAge;
    }


    public void setMaxAge (Integer maxAge) {
        this.maxAge = maxAge;
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
        Event event = (Event) o;
        return headcount == event.headcount &&
               ageToBe == event.ageToBe &&
               Objects.equals(id, event.id) &&
               Objects.equals(name, event.name) &&
               Objects.equals(roomUses, event.roomUses) &&
               Objects.equals(created, event.created) &&
               Objects.equals(updated, event.updated) &&
               eventType == event.eventType &&
               Objects.equals(customers, event.customers) &&
               Objects.equals(trainer, event.trainer) &&
               birthdayType == event.birthdayType &&
               Objects.equals(endOfApplication, event.endOfApplication) &&
               Objects.equals(price, event.price) &&
               Objects.equals(maxParticipants, event.maxParticipants) &&
               Objects.equals(description, event.description) &&
               Objects.equals(minAge, event.minAge) &&
               Objects.equals(maxAge, event.maxAge);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, name, roomUses, created, updated, eventType, customers, trainer,
                            headcount, ageToBe, birthdayType, endOfApplication, price,
                            maxParticipants, description, minAge, maxAge
        );
    }


    @Override
    public String toString () {
        return "Event{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", roomUses=" + roomUses +
               ", created=" + created +
               ", updated=" + updated +
               ", eventType=" + eventType +
               ", customers=" + customers +
               ", trainer=" + trainer.getFirstName() + trainer.getLastName() +
               ", headcount=" + headcount +
               ", ageToBe=" + ageToBe +
               ", birthdayType=" + birthdayType +
               ", endOfApplication=" + endOfApplication +
               ", price=" + price +
               ", maxParticipants=" + maxParticipants +
               ", description='" + description + '\'' +
               ", minAge=" + minAge +
               ", maxAge=" + maxAge +
               '}';
    }
}
