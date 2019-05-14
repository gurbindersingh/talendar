package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Customer {

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

    public Customer (){

    }


    public Customer (Long id, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String name) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
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
