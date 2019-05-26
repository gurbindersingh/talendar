package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Trainer trainer;

    @NotNull
    @Future
    private LocalDateTime holidayStart;

    @NotNull
    @Future
    private LocalDateTime holidayEnd;

    public Holiday(){}


    public Holiday (@NotNull Trainer trainer, @NotNull LocalDateTime holidayStart, @NotNull LocalDateTime holidayEnd) {
        this.trainer = trainer;
        this.holidayStart = holidayStart;
        this.holidayEnd = holidayEnd;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public Trainer getTrainer () {
        return trainer;
    }


    public void setTrainer (Trainer trainer) {
        this.trainer = trainer;
    }


    public LocalDateTime getHolidayStart () {
        return holidayStart;
    }


    public void setHolidayStart (LocalDateTime holidayStart) {
        this.holidayStart = holidayStart;
    }


    public LocalDateTime getHolidayEnd () {
        return holidayEnd;
    }


    public void setHolidayEnd (LocalDateTime holidayEnd) {
        this.holidayEnd = holidayEnd;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(id, holiday.id) &&
            Objects.equals(trainer, holiday.trainer) &&
            Objects.equals(holidayStart, holiday.holidayStart) &&
            Objects.equals(holidayEnd, holiday.holidayEnd);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, trainer, holidayStart, holidayEnd);
    }


    @Override
    public String toString () {
        return "Holiday{" +
            "id=" + id +
            ", trainerid=" + trainer+
            ", holidayStart=" + holidayStart +
            ", holidayEnd=" + holidayEnd +
            '}';
    }
}

