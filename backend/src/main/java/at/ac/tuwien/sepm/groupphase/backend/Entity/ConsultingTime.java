package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ConsultingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Trainer trainer;

    @NotNull
    private String title;
    private String description;

    @NotNull
    @Future
    private LocalDateTime consultingTimeStart;

    @NotNull
    @Future
    private LocalDateTime consultingTimeEnd;

    public ConsultingTime(){}


    public ConsultingTime(@NotNull Trainer trainer, @NotNull String title, String description, @NotNull LocalDateTime consultingTimeStart, @NotNull LocalDateTime consultingTimeEnd) {
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
        ConsultingTime that = (ConsultingTime) o;
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
        return "ConsultingTime{" +
               "id=" + id +
               ", trainer=" + trainer.getLastName() +
               trainer.getFirstName() +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", consultingTimeStart=" + consultingTimeStart +
               ", consultingTimeEnd=" + consultingTimeEnd +
               '}';
    }
}

