package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@SQLDelete(sql = "update Trainer t set t.deleted = true where t.id = ?")
@Where(clause = "deleted <> true")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String        firstName;
    @NotBlank
    @Column(nullable = false)
    private String        lastName;
    @NotNull
    @Past
    @Column(nullable = true)
    private LocalDate     birthday;
    @NotBlank
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$")
    @Column(nullable = false)
    private String        phone;
    @NotBlank
    @Email
    @Column(nullable = false)
    private String        email;
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Event>   events;
    @ElementCollection
    @CollectionTable(name = "birthday_types", joinColumns = { @JoinColumn(name = "trainer_id") })
    private List<String>  birthdayTypes;
    @OneToMany(mappedBy = "trainer",
               cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Holiday> holidays;

    @NotNull
    @Past
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDateTime updated;

    @NotNull
    @Column(nullable = false)
    private boolean deleted;


    public Trainer () {

    }


    public Trainer (Long id,
                    @NotBlank String firstName,
                    @NotBlank String lastName,
                    @NotNull @Past LocalDate birthday,
                    @NotBlank @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$") String phone,
                    @NotBlank @Email String email,
                    List<Event> events,
                    List<Holiday> holidays,
                    @NotNull List<String> birthdayTypes,
                    @NotNull @Past LocalDateTime created,
                    @NotNull @Past LocalDateTime updated
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.events = events;
        this.holidays = holidays;
        this.birthdayTypes = birthdayTypes;
        this.created = created;
        this.updated = updated;
        this.deleted = false;
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


    public List<Event> getEvents () {
        return events;
    }


    public void setEvents (List<Event> events) {
        this.events = events;
    }


    public List<String> getBirthdayTypes () {
        return birthdayTypes;
    }


    public List<Holiday> getHolidays () {
        return holidays;
    }


    public void setHolidays (List<Holiday> holidays) {
        this.holidays = holidays;
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


    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @PreRemove
    public void deleteUser() {
        this.deleted = true;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return deleted == trainer.deleted &&
               id.equals(trainer.id) &&
               firstName.equals(trainer.firstName) &&
               lastName.equals(trainer.lastName) &&
               birthday.equals(trainer.birthday) &&
               phone.equals(trainer.phone) &&
               email.equals(trainer.email) &&
               Objects.equals(events, trainer.events) &&
               Objects.equals(birthdayTypes, trainer.birthdayTypes) &&
               Objects.equals(holidays, trainer.holidays) &&
               created.equals(trainer.created) &&
               updated.equals(trainer.updated);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday, phone, email, events, birthdayTypes,
                            holidays, created, updated, deleted
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
               ", events=" + events +
               ", birthdayTypes=" + birthdayTypes +
               ", holidays=" + holidays +
               ", created=" + created +
               ", updated=" + updated +
               ", deleted=" + deleted +
               '}';
    }
}
