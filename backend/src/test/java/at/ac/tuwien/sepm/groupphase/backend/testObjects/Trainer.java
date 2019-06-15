package at.ac.tuwien.sepm.groupphase.backend.testObjects;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Trainer extends at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phone;
    private String email;
    private List<String> birthdayTypes;
    List<Event> events;
    List<Holiday> holidays;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Boolean deleted;

    public Trainer() {

    }

    public Trainer (Long id, String firstName,String lastName,LocalDate birthday, String phone,String email,LocalDateTime created,LocalDateTime updated, List<String> birthdayTypes, List<Event> events, List<Holiday> holidays) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.created = created;
        this.updated = updated;
        this.birthdayTypes = birthdayTypes;
        this.events = events;
        this.holidays = holidays;
        this.deleted = false;
    }


    @Override
    public List<String> getBirthdayTypes () {
        return birthdayTypes;
    }


    @Override
    public void setBirthdayTypes (List<String> birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public String getFirstName () {
        return firstName;
    }


    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }


    public String getLastName () {
        return lastName;
    }


    public void setLastName (String lastName) {
        this.lastName = lastName;
    }


    public LocalDate getBirthday () {
        return birthday;
    }


    public void setBirthday (LocalDate birthday) {
        this.birthday = birthday;
    }


    public String getPhone () {
        return phone;
    }


    public void setPhone (String phone) {
        this.phone = phone;
    }


    public String getEmail () {
        return email;
    }


    public void setEmail (String email) {
        this.email = email;
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
    public List<Event> getEvents () {
        return events;
    }


    @Override
    public void setEvents (List<Event> events) {
        this.events = events;
    }


    @Override
    public List<Holiday> getHolidays() {
        return holidays;
    }


    @Override
    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }


    public Boolean getDeleted() {
        return deleted;
    }


    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(id, trainer.id) &&
               Objects.equals(firstName, trainer.firstName) &&
               Objects.equals(lastName, trainer.lastName) &&
               Objects.equals(birthday, trainer.birthday) &&
               Objects.equals(phone, trainer.phone) &&
               Objects.equals(email, trainer.email) &&
               Objects.equals(birthdayTypes, trainer.birthdayTypes) &&
               Objects.equals(events, trainer.events) &&
               Objects.equals(holidays, trainer.holidays) &&
               Objects.equals(created, trainer.created) &&
               Objects.equals(updated, trainer.updated) &&
               Objects.equals(deleted, trainer.deleted);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, birthday, phone, email,
                            birthdayTypes, events, holidays, created, updated, deleted
        );
    }


    @Override
    public String toString() {
        return "Trainer{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", birthday=" + birthday +
               ", phone='" + phone + '\'' +
               ", email='" + email + '\'' +
               ", birthdayTypes=" + birthdayTypes +
               ", events=" + events +
               ", holidays=" + holidays +
               ", created=" + created +
               ", updated=" + updated +
               ", deleted=" + deleted +
               '}';
    }
}
