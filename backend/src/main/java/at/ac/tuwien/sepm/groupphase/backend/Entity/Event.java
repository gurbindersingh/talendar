package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true) //check how cascade works in all methods
    private List<RoomUse> roomUses = new LinkedList<>();
    @Column(name = "created", insertable = false, nullable = false, updatable = false)
    @Past
    private LocalDateTime created;
    @Column(name = "updated", nullable = false)
    @Past
    private LocalDateTime updated; //LocalDateTime > Date
    @Column(name = "event_type", nullable = false)
    private EventType eventType;
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Customer> customers;

    /*
        These Variables are used by non Rent Types
     */

    @Column(name = "trainer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Trainer trainer;


    /*
        These Variables are birthday specific
     */

    @Column
    private int headcount;
    @Column
    private int ageToBe;
    @Column
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


    public Event (Long id, @NotBlank String name, @NotNull List<RoomUse> roomUses, @Past LocalDateTime created, @Past LocalDateTime updated, EventType eventType, @NotNull List<Customer> customers, Trainer trainer, int headcount, int ageToBe, BirthdayType birthdayType) {
        this.id = id;
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


    public List<Customer> getCustomers () {
        return customers;
    }


    public void setCustomers (List<Customer> customers) {
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
            birthdayType == event.birthdayType;
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, name, roomUses, created, updated, eventType, customers, trainer, headcount, ageToBe, birthdayType);
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
            ", trainer=" + trainer +
            ", headcount=" + headcount +
            ", ageToBe=" + ageToBe +
            ", birthdayType=" + birthdayType +
            '}';
    }
}
