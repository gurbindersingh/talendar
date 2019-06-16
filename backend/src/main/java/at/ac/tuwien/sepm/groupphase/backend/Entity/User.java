package at.ac.tuwien.sepm.groupphase.backend.Entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Where(clause = "deleted <> true")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private Long id;

    /**
     * Minimal Personal Information
     */
    @NotBlank
    @Column(nullable = false)
    private String firstName;
    @NotBlank
    @Column(nullable = false)
    private String lastName;
    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate birthday;

    /**
     * Credentials For User
     */
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false, name = "password")
    private String password;

    /**
     * Meta Informations
     */
    @NotNull
    private boolean admin;
    @NotNull
    private boolean deleted;
    @NotNull
    @Past
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDateTime updated;


    public User() {
    }


    // TODO id was set previously in trainer
    public User(Long id,
                @NotBlank String firstName,
                @NotBlank String lastName,
                @NotNull @Past LocalDate birthday,
                @NotBlank @Email String email,
                @NotBlank String password,
                @NotNull @Past LocalDateTime created,
                @NotNull @Past LocalDateTime updated
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.created = created;
        this.updated = updated;
        this.deleted = false;
        this.admin = false;
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


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAdmin() {
        return admin;
    }


    public void setAdmin(boolean admin) {
        this.admin = admin;
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


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!( o instanceof User )) return false;
        User user = (User) o;
        return admin == user.admin &&
               deleted == user.deleted &&
               Objects.equals(id, user.id) &&
               Objects.equals(firstName, user.firstName) &&
               Objects.equals(lastName, user.lastName) &&
               Objects.equals(birthday, user.birthday) &&
               Objects.equals(email, user.email) &&
               Objects.equals(password, user.password) &&
               Objects.equals(created, user.created) &&
               Objects.equals(updated, user.updated);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday, email, password, admin, deleted,
                            created,
                            updated
        );
    }


    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", birthday=" + birthday +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", admin=" + admin +
               ", deleted=" + deleted +
               ", created=" + created +
               ", updated=" + updated +
               '}';
    }
}
