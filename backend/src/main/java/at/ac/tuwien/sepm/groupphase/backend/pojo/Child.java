package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Child extends Customer {

    @Min(1)
    @Max(12)
    private Integer grade;

    public Child(){

    }
    public Child (Long id, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String name, @Min(1) @Max(12) Integer grade) {
        super(id, email, phone, name);
        this.grade = grade;
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
        if(!super.equals(o)) return false;
        Child child = (Child) o;
        return Objects.equals(grade, child.grade);
    }


    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), grade);
    }


    @Override
    public String toString () {
        return "Child{" +
            "grade=" + grade +
            "} " + super.toString();
    }
}
