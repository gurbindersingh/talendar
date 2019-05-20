package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class CustomerDto {
    private Long id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;


    public CustomerDto(){

    }
    public CustomerDto (Long id, String email, String phone, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
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


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, email, phone, firstName, lastName);
    }


    @Override
    public String toString () {
        return "CustomerDto{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}
