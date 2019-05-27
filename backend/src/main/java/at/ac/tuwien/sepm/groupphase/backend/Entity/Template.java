package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

// annotation needed to persist classes with JPA
@Entity
public class Template {


    //make it a primary Key (Identity means, unique keys per table, nut diff tables share Values)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100, message = "this msg is shown upon validation errors")
    @NotBlank
    private String test;

    public Template() {

    }


    public Template (@Size(max = 100, message = "this msg is shown upon validation errors") @NotBlank String test) {
        this.test = test;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public String getTest () {
        return test;
    }


    public void setTest (String test) {
        this.test = test;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return Objects.equals(id, template.id) &&
            Objects.equals(test, template.test);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, test);
    }


    @Override
    public String toString () {
        return "Template{" +
            "id=" + id +
            ", test='" + test + '\'' +
            '}';
    }
}
