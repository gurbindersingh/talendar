package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;

public class ConsultingTimeDto {



    private Long id;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "consultingTimes", "events"})
    private Trainer trainer;
    private String title;
    private String description;
    private LocalDateTime consultingTimeStart;
    private LocalDateTime consultingTimeEnd;

    public ConsultingTimeDto() {

    }


    public ConsultingTimeDto(Long id, Trainer trainer, String title, String description,
                             LocalDateTime consultingTimeStart,
                             LocalDateTime consultingTimeEnd
    ) {
        this.id = id;
        this.trainer = trainer;
        this.title = title;
        this.description = description;
        this.consultingTimeStart = consultingTimeStart;
        this.consultingTimeEnd = consultingTimeEnd;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Trainer getTrainer() {
        return trainer;
    }


    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
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


    public LocalDateTime getConsultingTimeStart() {
        return consultingTimeStart;
    }


    public void setConsultingTimeStart(LocalDateTime consultingTimeStart) {
        this.consultingTimeStart = consultingTimeStart;
    }


    public LocalDateTime getConsultingTimeEnd() {
        return consultingTimeEnd;
    }


    public void setConsultingTimeEnd(LocalDateTime consultingTimeEnd) {
        this.consultingTimeEnd = consultingTimeEnd;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ConsultingTimeDto that = (ConsultingTimeDto) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(trainer, that.trainer) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(consultingTimeStart, that.consultingTimeStart) &&
               Objects.equals(consultingTimeEnd, that.consultingTimeEnd);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, trainer, title, description, consultingTimeStart,
                            consultingTimeEnd
        );
    }


    @Override
    public String toString() {
        return "ConsultingTimeDto{" +
               "id=" + id +
               ", trainer=" + trainer +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", consultingTimeStart=" + consultingTimeStart +
               ", consultingTimeEnd=" + consultingTimeEnd +
               '}';
    }
}
