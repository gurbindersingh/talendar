package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

// TODO Validation for phone
// TODO review current min/max constraint on birthday
// TODO property for image? (type: String? path to location on server)

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Past
    private LocalDate birthday;
    @NotBlank
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")
    private String phone;
    @NotBlank
    @Email
    private String email;

    @NotNull
    @Past
    private LocalDateTime created;
    @NotNull
    @Past
    private LocalDateTime updated;

    public Trainer() {
        
    }

    public Trainer (@NotBlank String firstName, @NotBlank String lastName, @Past LocalDate birthday, String phone, @Email String email, @Past LocalDateTime created, @Past LocalDateTime updated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
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
        Trainer trainer = (Trainer) o;
        return Objects.equals(id, trainer.id) &&
            Objects.equals(firstName, trainer.firstName) &&
            Objects.equals(lastName, trainer.lastName) &&
            Objects.equals(birthday, trainer.birthday) &&
            Objects.equals(phone, trainer.phone) &&
            Objects.equals(email, trainer.email) &&
            Objects.equals(created, trainer.created) &&
            Objects.equals(updated, trainer.updated);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, firstName, lastName, birthday, phone, email, created, updated);
    }


    @Override
    public String toString () {
        return "Trainer{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birthday=" + birthday +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
