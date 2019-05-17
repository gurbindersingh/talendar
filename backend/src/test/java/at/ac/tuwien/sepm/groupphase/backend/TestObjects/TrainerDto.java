package at.ac.tuwien.sepm.groupphase.backend.TestObjects;

import java.time.LocalDateTime;
import java.util.Objects;

public class TrainerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String phone;
    private String email;
    private LocalDateTime created;
    private LocalDateTime updated;

    public TrainerDto () {

    }


    public TrainerDto (String firstName, String lastName, Integer age, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
        this.email = email;
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


    public Integer getAge () {
        return age;
    }


    public void setAge (Integer age) {
        this.age = age;
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
        TrainerDto that = (TrainerDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(age, that.age) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(created, that.created) &&
            Objects.equals(updated, that.updated);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, firstName, lastName, age, phone, email, created, updated);
    }


    @Override
    public String toString () {
        return "TrainerDto{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", age=" + age +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
