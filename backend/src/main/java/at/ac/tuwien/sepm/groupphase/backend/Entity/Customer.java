package at.ac.tuwien.sepm.groupphase.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "customer_type")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @ManyToMany(mappedBy = "customers" )
    @JsonIgnoreProperties("customers")
    private Set<Event> events;


    public Customer (){

    }
    public Customer (Long id, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String firstName, @NotBlank String lastName, Set<Event> events) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.events = events;
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


    public Set<Event> getEvents () {
        return events;
    }


    public void setEvents (Set<Event> events) {
        this.events = events;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
            Objects.equals(email, customer.email) &&
            Objects.equals(phone, customer.phone) &&
            Objects.equals(firstName, customer.firstName) &&
            Objects.equals(lastName, customer.lastName);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, email, phone, firstName, lastName);
    }


    @Override
    public String toString () {
        return "Customer{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}
