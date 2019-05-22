package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TrainerDto{

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phone;
    private String email;
    private List<EventDto> events;
    private List<String> birthdayTypes;
    private LocalDateTime created;
    private LocalDateTime updated;

    public TrainerDto() {

    }


    public TrainerDto (Long id, String firstName, String lastName, LocalDate birthday, String phone, String email, List<EventDto> events, List<String> birthdayTypes, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.events = events;
        this.created = created;
        this.updated = updated;
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


    public List<EventDto> getEvents () {
        return events;
    }


    public void setEvents (List<EventDto> events) {
        this.events = events;
    }


    public List<String> getBirthdayTypes () {
        return birthdayTypes;
    }


    public void setBirthdayTypes (List<String> birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
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
        TrainerDto that = (TrainerDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(events, that.events) &&
            Objects.equals(birthdayTypes, that.birthdayTypes) &&
            Objects.equals(created, that.created) &&
            Objects.equals(updated, that.updated);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, firstName, lastName, birthday, phone, email, events, birthdayTypes, created, updated);
    }


    @Override
    public String toString () {
        return "TrainerDto{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birthday=" + birthday +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", events=" + events +
            ", birthdayTypes=" + birthdayTypes +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
