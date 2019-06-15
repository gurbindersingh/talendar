package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;

public class HolidayDto {



    private Long id;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "holidays", "events"})
    private Trainer trainer;
    private LocalDateTime holidayStart;
    private LocalDateTime holidayEnd;

    public HolidayDto () {

    }


    public HolidayDto (Long id, Trainer trainer, LocalDateTime holidayStart, LocalDateTime holidayEnd) {
        this.id = id;
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
        HolidayDto that = (HolidayDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(trainer, that.trainer) &&
            Objects.equals(holidayStart, that.holidayStart) &&
            Objects.equals(holidayEnd, that.holidayEnd);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, trainer, holidayStart, holidayEnd);
    }


    @Override
    public String toString () {
        return "HolidayDto{" +
            "id=" + id +
            ", trainerid=" + trainer +
            ", holidayStart=" + holidayStart +
            ", holidayEnd=" + holidayEnd +
            '}';
    }
}
