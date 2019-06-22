package at.ac.tuwien.sepm.groupphase.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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
    @Column(name = "email")
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @ManyToMany(mappedBy = "customers", fetch = FetchType.EAGER )
    @JsonIgnoreProperties("customers")
    private Set<Event> events;

    @Column
    private Integer emailId;

    @Column
    private String childName;

    @Column
    private String childLastName;

    @Column
    private LocalDateTime birthOfChild;

    @Column(name = "wants_email")
    private Boolean wantsEmail;


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


    public Customer(
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String firstName,
        @NotBlank String lastName,
        Set<Event> events,
        Integer emailId,
        String childName,
        String childLastName,
        LocalDateTime birthOfChild,
        Boolean wantsEmail
    ) {
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.events = events;
        this.emailId = emailId;
        this.childName = childName;
        this.childLastName = childLastName;
        this.birthOfChild = birthOfChild;
        this.wantsEmail = wantsEmail;
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


    public Integer getEmailId() {
        return emailId;
    }


    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }


    public String getChildName() {
        return childName;
    }


    public void setChildName(String childName) {
        this.childName = childName;
    }


    public String getChildLastName() {
        return childLastName;
    }


    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }


    public LocalDateTime getBirthOfChild() {
        return birthOfChild;
    }


    public void setBirthOfChild(LocalDateTime birthOfChild) {
        this.birthOfChild = birthOfChild;
    }


    public Boolean getWantsEmail() {
        return wantsEmail;
    }


    public void setWantsEmail(Boolean wantsEmail) {
        this.wantsEmail = wantsEmail;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email) &&
               Objects.equals(phone, customer.phone) &&
               Objects.equals(firstName, customer.firstName) &&
               Objects.equals(emailId, customer.emailId) &&
               Objects.equals(childName, customer.childName) &&
               Objects.equals(childLastName, customer.childLastName) &&
               Objects.equals(birthOfChild, customer.birthOfChild) &&
               Objects.equals(wantsEmail, customer.wantsEmail);
    }


    @Override
    public int hashCode() {
        return Objects.hash(email, phone, firstName, emailId, childName, childLastName,
                            birthOfChild, wantsEmail
        );
    }


    @Override
    public String toString() {
        return "Customer{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", emailId=" + emailId +
               ", childName='" + childName + '\'' +
               ", childLastName='" + childLastName + '\'' +
               ", birthOfChild=" + birthOfChild +
               ", wantsEmail=" + wantsEmail +
               '}';
    }
}
