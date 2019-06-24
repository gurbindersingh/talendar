package at.ac.tuwien.sepm.groupphase.backend.testObjects;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class CustomerDto {


    private Long id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private Set<EventDto> events;
    private Integer emailId;

    private String childName;

    private String childLastName;

    private LocalDateTime birthOfChild;

    private Boolean wantsEmail;

    public CustomerDto() {

    }


    public CustomerDto(Long id, String email, String phone, String firstName, String lastName,
                       Set<EventDto> events,
                       Integer emailId,
                       String childName,
                       String childLastName,
                       LocalDateTime birthOfChild,
                       Boolean wantsEmail
    ) {
        this.id = id;
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


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Set<EventDto> getEvents() {
        return events;
    }


    public void setEvents(Set<EventDto> events) {
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
        if(!( o instanceof CustomerDto )) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(email, that.email) &&
               Objects.equals(phone, that.phone) &&
               Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName) &&
               Objects.equals(events, that.events) &&
               Objects.equals(emailId, that.emailId) &&
               Objects.equals(childName, that.childName) &&
               Objects.equals(childLastName, that.childLastName) &&
               Objects.equals(birthOfChild, that.birthOfChild) &&
               Objects.equals(wantsEmail, that.wantsEmail);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, firstName, lastName, events, emailId, childName,
                            childLastName, birthOfChild, wantsEmail
        );
    }


    @Override
    public String toString() {
        return "CustomerDto{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", events=" + events +
               ", emailId=" + emailId +
               ", childName='" + childName + '\'' +
               ", childLastName='" + childLastName + '\'' +
               ", birthOfChild=" + birthOfChild +
               ", wantsEmail=" + wantsEmail +
               '}';
    }
}
