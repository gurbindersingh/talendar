package at.ac.tuwien.sepm.groupphase.backend.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Integer trainerid;

    @NotNull
    @Future
    private LocalDateTime holidayStart;

    @NotNull
    @Future
    private LocalDateTime holidayEnd;

    public Holiday(){}


    public Holiday (@NotNull Integer trainerid, @NotNull @Future LocalDateTime holidayStart, @NotNull @Future LocalDateTime holidayEnd) {
        this.trainerid = trainerid;
        this.holidayStart = holidayStart;
        this.holidayEnd = holidayEnd;
    }


    public Long getId () {
        return id;
    }


    public void setId (Long id) {
        this.id = id;
    }


    public Integer getTrainerid () {
        return trainerid;
    }


    public void setTrainerid (Integer trainerid) {
        this.trainerid = trainerid;
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
            Objects.equals(trainerid, holiday.trainerid) &&
            Objects.equals(holidayStart, holiday.holidayStart) &&
            Objects.equals(holidayEnd, holiday.holidayEnd);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, trainerid, holidayStart, holidayEnd);
    }


    @Override
    public String toString () {
        return "Holiday{" +
            "id=" + id +
            ", trainerid=" + trainerid +
            ", holidayStart=" + holidayStart +
            ", holidayEnd=" + holidayEnd +
            '}';
    }
}

