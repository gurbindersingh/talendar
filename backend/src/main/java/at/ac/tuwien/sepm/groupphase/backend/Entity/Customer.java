package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "customer_type")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String name;


    public Customer (){

    }
    public Customer (Integer id, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String name) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
    }


    public Integer getId () {
        return id;
    }


    public void setId (Integer id) {
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


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
            Objects.equals(email, customer.email) &&
            Objects.equals(phone, customer.phone) &&
            Objects.equals(name, customer.name);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, email, phone, name);
    }


    @Override
    public String toString () {
        return "Customer{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
