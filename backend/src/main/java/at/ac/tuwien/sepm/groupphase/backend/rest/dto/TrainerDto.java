package at.ac.tuwien.sepm.groupphase.backend.rest.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TrainerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phone;
    private String email;
    @JsonIgnoreProperties("trainer")
    private List<EventDto> events;
    private List<String> birthdayTypes;
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "trainer" })
    private List<HolidayDto> holidays;
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "trainer" })
    private List<ConsultingTimeDto> consultingTimes;
    // a path to the place where this picture is stored on this server
    private String picture;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String password;
    private Boolean deleted;


    public TrainerDto() {

    }


    public TrainerDto (Long id,
                       String firstName,
                       String lastName,
                       LocalDate birthday,
                       String phone,
                       String email,
                       List<EventDto> events,
                       List<String> birthdayTypes,
                       List<HolidayDto> holidays,
                       List<ConsultingTimeDto> consultingTimes,
                       String picture,
                       String password,
                       LocalDateTime created,
                       LocalDateTime updated
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.events = events;
        this.birthdayTypes = birthdayTypes;
        this.holidays = holidays;
        this.consultingTimes = consultingTimes;
        this.picture = picture;
        this.password = password;
        this.created = created;
        this.updated = updated;
        this.deleted = false;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public LocalDate getBirthday() {
        return birthday;
    }


    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public List<EventDto> getEvents() {
        return events;
    }


    public void setEvents(List<EventDto> events) {
        this.events = events;
    }


    public List<String> getBirthdayTypes() {
        return birthdayTypes;
    }


    public void setBirthdayTypes(List<String> birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
    }


    public List<HolidayDto> getHolidays() {
        return holidays;
    }


    public void setHolidays(List<HolidayDto> holidays) {
        this.holidays = holidays;
    }


    public String getPicture() {
        return picture;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public LocalDateTime getCreated () {
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


    public Boolean getDeleted() {
        return deleted;
    }


    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public List<ConsultingTimeDto> getConsultingTimes() {
        return consultingTimes;
    }


    public void setConsultingTimes(
        List<ConsultingTimeDto> consultingTimes
    ) {
        this.consultingTimes = consultingTimes;
    }


    @Override
    public boolean equals(Object o) {
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
               Objects.equals(holidays, that.holidays) &&
               Objects.equals(consultingTimes, that.consultingTimes) &&
               Objects.equals(picture, that.picture) &&
               Objects.equals(password, that.password) &&
               Objects.equals(created, that.created) &&
               Objects.equals(updated, that.updated) &&
               Objects.equals(deleted, that.deleted);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id,
                            firstName,
                            lastName,
                            birthday,
                            phone,
                            email,
                            events,
                            birthdayTypes,
                            holidays,
                            consultingTimes,
                            picture,
                            password,
                            created,
                            updated,
                            deleted
        );
    }


    @Override
    public String toString() {
        return "TrainerDto{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", birthday=" + birthday +
               ", phone='" + phone + '\'' +
               ", email='" + email + '\'' +
               ", events=" + events +
               ", birthdayTypes=" + birthdayTypes +
               ", holidays=" + holidays +
               ", consultingTimes" + consultingTimes +
               ", picture=" + picture +
               // currently password is not displayed!
               ", created=" + created +
               ", updated=" + updated +
               ", deleted=" + deleted +
               '}';
    }
}
