package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class HolidayDto {



    private Long id;
    private Integer trainerid;
    private LocalDateTime holidayStart;
    private LocalDateTime holidayEnd;

    public HolidayDto () {

    }


    public HolidayDto (Long id, Integer trainerid, LocalDateTime holidayStart, LocalDateTime holidayEnd) {
        this.id = id;
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
        HolidayDto that = (HolidayDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(trainerid, that.trainerid) &&
            Objects.equals(holidayStart, that.holidayStart) &&
            Objects.equals(holidayEnd, that.holidayEnd);
    }


    @Override
    public int hashCode () {
        return Objects.hash(id, trainerid, holidayStart, holidayEnd);
    }


    @Override
    public String toString () {
        return "HolidayDto{" +
            "id=" + id +
            ", trainerid=" + trainerid +
            ", holidayStart=" + holidayStart +
            ", holidayEnd=" + holidayEnd +
            '}';
    }
}
