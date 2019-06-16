package at.ac.tuwien.sepm.groupphase.backend.Entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
public class Trainer extends User {

    @NotBlank
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$")
    @Column(nullable = false)
    private String phone;
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Event> events;
    @ElementCollection
    @CollectionTable(name = "birthday_types", joinColumns = { @JoinColumn(name = "trainer_id") })
    private List<String> birthdayTypes;
    @OneToMany(mappedBy = "trainer",
               cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Holiday> holidays;




    public Trainer() {

    }


    public Trainer(Long id,
                   @NotBlank String firstName,
                   @NotBlank String lastName,
                   @NotNull @Past LocalDate birthday,
                   @NotBlank @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$") String phone,
                   @NotBlank @Email String email,
                   @NotBlank String password,
                   List<Event> events,
                   List<Holiday> holidays,
                   @NotNull List<String> birthdayTypes,
                   @NotNull @Past LocalDateTime created,
                   @NotNull @Past LocalDateTime updated
    ) {
        super(id, firstName, lastName, birthday, email, password, created, updated);
        this.phone = phone;
        this.events = events;
        this.holidays = holidays;
        this.birthdayTypes = birthdayTypes;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public List<Event> getEvents() {
        return events;
    }


    public void setEvents(List<Event> events) {
        this.events = events;
    }


    public List<String> getBirthdayTypes() {
        return birthdayTypes;
    }


    public List<Holiday> getHolidays() {
        return holidays;
    }


    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }


    public void setBirthdayTypes(List<String> birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!( o instanceof Trainer )) return false;
        if(!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(phone, trainer.phone) &&
               Objects.equals(events, trainer.events) &&
               Objects.equals(birthdayTypes, trainer.birthdayTypes) &&
               Objects.equals(holidays, trainer.holidays);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phone, events, birthdayTypes, holidays);
    }


    @Override
    public String toString() {
        return "Trainer{" +
               "phone='" + phone + '\'' +
               ", events=" + events +
               ", birthdayTypes=" + birthdayTypes +
               ", holidays=" + holidays +
               '}';
    }
}
