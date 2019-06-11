package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false, name = "password")
    private String password;

    @NotNull
    private boolean admin;
    @NotNull
    private boolean deleted;

    @OneToOne
    private Trainer trainer;

    public User() {}

    public User(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull boolean deleted,
        @NotNull boolean admin,
        Trainer trainer
    ) {
        this.email = email;
        this.password = password;
        this.deleted = deleted;
        this.admin = admin;
        this.trainer = trainer;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public Trainer getTrainer() {
        return trainer;
    }


    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }


    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public boolean isAdmin() {
        return admin;
    }


    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!( o instanceof User )) return false;
        User user = (User) o;
        return deleted == user.deleted &&
               admin == user.admin &&
               Objects.equals(id, user.id) &&
               Objects.equals(email, user.email) &&
               Objects.equals(password, user.password) &&
               Objects.equals(trainer, user.trainer);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, deleted, admin, trainer);
    }


    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", admin=" + admin +
               ", deleted=" + deleted +
               ", trainer=" + trainer +
               '}';
    }
}
