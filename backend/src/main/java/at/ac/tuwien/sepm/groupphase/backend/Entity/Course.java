package at.ac.tuwien.sepm.groupphase.backend.Entity;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("Course")
public class Course extends Event {


    @NotNull
    @Column(name = "end_of_application")
    private LocalDateTime endOfApplication;
    private Double price;

    @NotNull
    @Column(name = "max_participants")
    private Integer maxParticipants;

    @NotNull
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private List<Customer> customer;


    public Course(){

    }


    public Course (Integer id, @NotBlank String name, @NotNull LinkedList<RoomUse> roomUses, @NotNull @Past LocalDateTime created, @NotNull @Past LocalDateTime updated, @NotNull LocalDateTime endOfApplication, Double price, Integer maxParticipants, @NotNull String description, @NotNull Trainer trainer, @NotNull LinkedList<Customer> customer) {
        super(id, name, roomUses, created, updated);
        this.endOfApplication = endOfApplication;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.trainer = trainer;
        this.customer = customer;
    }


    public LocalDateTime getEndOfApplication () {
        return endOfApplication;
    }


    public void setEndOfApplication (LocalDateTime endOfApplication) {
        this.endOfApplication = endOfApplication;
    }


    public Double getPrice () {
        return price;
    }


    public void setPrice (Double price) {
        this.price = price;
    }


    public Integer getMaxParticipants () {
        return maxParticipants;
    }


    public void setMaxParticipants (Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public String getDescription () {
        return description;
    }


    public void setDescription (String description) {
        this.description = description;
    }


    public Trainer getTrainer () {
        return trainer;
    }


    public void setTrainer (Trainer trainer) {
        this.trainer = trainer;
    }


    public List<Customer> getCustomer () {
        return customer;
    }


    public void setCustomer (List<Customer> customer) {
        this.customer = customer;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        Course course = (Course) o;
        return Objects.equals(endOfApplication, course.endOfApplication) &&
            Objects.equals(price, course.price) &&
            Objects.equals(maxParticipants, course.maxParticipants) &&
            Objects.equals(description, course.description) &&
            Objects.equals(trainer, course.trainer) &&
            Objects.equals(customer, course.customer);
    }


    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), endOfApplication, price, maxParticipants, description, trainer, customer);
    }


    @Override
    public String toString () {
        return "Course{" +
            "endOfApplication=" + endOfApplication +
            ", price=" + price +
            ", maxParticipants=" + maxParticipants +
            ", description='" + description + '\'' +
            ", trainer=" + trainer +
            ", customer=" + customer +
            '}';
    }
}
