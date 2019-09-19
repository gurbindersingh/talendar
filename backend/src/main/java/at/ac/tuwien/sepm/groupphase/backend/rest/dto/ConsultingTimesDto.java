package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class ConsultingTimesDto {

    private Long trainerid;
    private String title;
    private String description;
    private String cronExpression;

    public ConsultingTimesDto() {

    }

    public ConsultingTimesDto(Long trainerid, String title, String description, String cronExpression) {
        this.trainerid = trainerid;
        this.title = title;
        this.description = description;
        this.cronExpression = cronExpression;
    }


    public Long getTrainerid() {
        return trainerid;
    }


    public void setTrainerid(Long trainerid) {
        this.trainerid = trainerid;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
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
        ConsultingTimesDto that = (ConsultingTimesDto) o;
        return Objects.equals(trainerid, that.trainerid) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(cronExpression, that.cronExpression);
    }


    @Override
    public int hashCode() {
        return Objects.hash(trainerid, title, description, cronExpression);
    }


    @Override
    public String toString() {
        return "ConsultingTimesDto{" +
               "trainerid=" + trainerid +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", cronExpression='" + cronExpression + '\'' +
               '}';
    }
}
