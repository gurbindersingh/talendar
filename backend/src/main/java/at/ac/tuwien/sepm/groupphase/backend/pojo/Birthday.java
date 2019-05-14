package at.ac.tuwien.sepm.groupphase.backend.pojo;

import at.ac.tuwien.sepm.groupphase.backend.pojo.enums.BirthdayType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;

@Entity
public class Birthday extends Event {

    @Column(name = "head_count")
    private Integer headCount;
    @NotNull
    private Customer guardian;
    @Column(name = "average_age")
    private Integer avgAge;
    @NotNull
    @ManyToOne
    private Trainer trainer;
    @NotNull
    private BirthdayType type;

    public Birthday(){

    }


    public Birthday (Long id, @NotBlank String name, @NotNull LinkedList<RoomUse> roomUses, @NotNull @Past LocalDateTime created, @NotNull @Past LocalDateTime updated, Integer headCount, @NotNull Customer guardian, Integer avgAge, @NotNull Trainer trainer, @NotNull BirthdayType type) {
        super(id, name, roomUses, created, updated);
        this.headCount = headCount;
        this.guardian = guardian;
        this.avgAge = avgAge;
        this.trainer = trainer;
        this.type = type;
    }


    public Integer getHeadCount () {
        return headCount;
    }


    public void setHeadCount (Integer headCount) {
        this.headCount = headCount;
    }


    public Customer getGuardian () {
        return guardian;
    }


    public void setGuardian (Customer guardian) {
        this.guardian = guardian;
    }


    public Integer getAvgAge () {
        return avgAge;
    }


    public void setAvgAge (Integer avgAge) {
        this.avgAge = avgAge;
    }


    public Trainer getTrainer () {
        return trainer;
    }


    public void setTrainer (Trainer trainer) {
        this.trainer = trainer;
    }


    public BirthdayType getType () {
        return type;
    }


    public void setType (BirthdayType type) {
        this.type = type;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        Birthday birthday = (Birthday) o;
        return Objects.equals(headCount, birthday.headCount) &&
            Objects.equals(guardian, birthday.guardian) &&
            Objects.equals(avgAge, birthday.avgAge) &&
            Objects.equals(trainer, birthday.trainer) &&
            type == birthday.type;
    }


    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), headCount, guardian, avgAge, trainer, type);
    }


    @Override
    public String toString () {
        return "Birthday{" +
            "headCount=" + headCount +
            ", guardian=" + guardian +
            ", avgAge=" + avgAge +
            ", trainer=" + trainer +
            ", type=" + type +
            "} " + super.toString();
    }
}