package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class HolidaysDto {

    private Long trainerid;
    private String cronExpression;

    public HolidaysDto() {

    }

    public HolidaysDto(Long trainerid, String cronExpression) {
        this.trainerid = trainerid;
        this.cronExpression = cronExpression;
    }

    public Long getTrainerid() {
        return trainerid;
    }


    public void setTrainerid(Long trainerid) {
        this.trainerid = trainerid;
    }


    public String getCronExpression() {
        return cronExpression;
    }


    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        HolidaysDto that = (HolidaysDto) o;
        return Objects.equals(trainerid, that.trainerid) &&
               Objects.equals(cronExpression, that.cronExpression);
    }


    @Override
    public int hashCode() {
        return Objects.hash(trainerid, cronExpression);
    }


    @Override
    public String toString() {
        return "HolidaysDto{" +
               "trainerid=" + trainerid +
               ", cronExpression='" + cronExpression + '\'' +
               '}';
    }
}
