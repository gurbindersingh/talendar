package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String name;
    @Min(1)
    @Max(12)
    private Integer grade;

    public Participant (){

    }
    public Participant (Long id, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String name, @Min(1) @Max(12) Integer grade) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.grade = grade;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public String getEmail () {
        return email;
    }


    public void setEmail (String email) {
        this.email = email;
    }


    public String getPhone () {
        return phone;
    }


    public void setPhone (String phone) {
        this.phone = phone;
    }


    public String getName () {
        return name;
    }


    public void setName (String name) {
        this.name = name;
    }


    public Integer getGrade () {
        return grade;
    }


    public void setGrade (Integer grade) {
        this.grade = grade;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(name, that.name) &&
            Objects.equals(grade, that.grade);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, email, phone, name, grade);
    }


    @Override
    public String toString () {
        return "Participant{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", name='" + name + '\'' +
            ", grade=" + grade +
            '}';
    }
}
