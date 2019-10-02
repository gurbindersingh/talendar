package at.ac.tuwien.sepm.groupphase.backend.Entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
public class Trainer extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$")
    @Column(nullable = false)
    private String phone;
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Event> events;
    @ElementCollection
    @CollectionTable(name = "birthday_types", joinColumns = { @JoinColumn(name = "trainer_id") })
    private List<String> birthdayTypes;
    @OneToMany(mappedBy = "trainer",
               cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Holiday> holidays;
    @OneToMany(mappedBy = "trainer",
               cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<ConsultingTime> consultingTimes;
    // the location where the profile picture of this trainer is stored
    private String picture;
    private Double consultationPrice;




    public Trainer() {

    }


    public Trainer(Long id,
                   @NotBlank String firstName,
                   @NotBlank String lastName,
                   @NotNull @Past LocalDate birthday,
                   @NotBlank @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$") String phone,
                   @NotBlank @Email String email,
                   @NotBlank String password,
                   List<Event> events,
                   List<Holiday> holidays,
                   List<ConsultingTime> consultingTimes,
                   String picture,
                   Double consultationPrice,
                   @NotNull List<String> birthdayTypes,
                   @NotNull @Past LocalDateTime created,
                   @NotNull @Past LocalDateTime updated
    ) {
        super(firstName, lastName, birthday, email, password, created, updated);
        this.id = id;
        this.phone = phone;
        this.events = events;
        this.holidays = holidays;
        this.picture = picture;
        this.birthdayTypes = birthdayTypes;
        this.consultationPrice = consultationPrice;
    }

    public Double getConsultationPrice() {
        return consultationPrice;
    }


    public void setConsultationPrice(Double consultationPrice) {
        this.consultationPrice = consultationPrice;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public List<Event> getEvents() {
        return events;
    }


    public void setEvents(List<Event> events) {
        this.events = events;
    }


    public List<String> getBirthdayTypes() {
        return birthdayTypes;
    }


    public List<Holiday> getHolidays() {
        return holidays;
    }


    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }


    public void setBirthdayTypes(List<String> birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
    }


    public String getPicture() {
        return picture;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }


    public List<ConsultingTime> getConsultingTimes() {
        return consultingTimes;
    }


    public void setConsultingTimes(
        List<ConsultingTime> consultingTimes
    ) {
        this.consultingTimes = consultingTimes;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!( o instanceof Trainer )) return false;
        if(!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return id == trainer.id &&
            Objects.equals(phone, trainer.phone) &&
               Objects.equals(events, trainer.events) &&
               Objects.equals(birthdayTypes, trainer.birthdayTypes) &&
               Objects.equals(holidays, trainer.holidays) &&
               Objects.equals(consultingTimes, trainer.consultingTimes) &&
               picture.equals(trainer.picture);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, phone, events, birthdayTypes,
                            holidays, consultingTimes, picture
        );
    }


    @Override
    public String toString() {
        return "Trainer{" +
               "id=" + id +
               "phone='" + phone + '\'' +
               ", events=" + events +
               ", birthdayTypes=" + birthdayTypes +
               ", holidays=" + holidays +
               ", consultingTimes" + consultingTimes +
               ", picture=" + picture +
               ", firstname=" + getFirstName() +
               ", lastname=" + getLastName() +
               '}';
    }
}
