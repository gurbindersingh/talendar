package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class CustomerDto {

    private Integer id;
    private String email;
    private String phone;
    private String name;


    public CustomerDto () {
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
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(name, that.name);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, email, phone, name);
    }


    @Override
    public String toString () {
        return "CustomerDto{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
