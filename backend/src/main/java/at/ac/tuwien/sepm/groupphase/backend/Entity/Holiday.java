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

    private Long groupId;

    @NotNull
    @ManyToOne
    private Trainer trainer;

    @NotNull
    private String title;
    private String description;

    @NotNull
    @Future
    private LocalDateTime holidayStart;

    @NotNull
    @Future
    private LocalDateTime holidayEnd;

    public Holiday(){}


    public Holiday (@NotNull Trainer trainer, @NotNull String title, String description, @NotNull LocalDateTime holidayStart, @NotNull LocalDateTime holidayEnd, Long groupID) {
        this.trainer = trainer;
        this.title = title;
        this.description = description;
        this.holidayStart = holidayStart;
        this.holidayEnd = holidayEnd;
        this.groupId = groupID;
    }


    public Long getGroupID() {
        return groupId;
    }


    public void setGroupID(Long groupID) {
        this.groupId = groupID;
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


    public LocalDateTime getHolidayStart() {
        return holidayStart;
    }


    public void setHolidayStart(LocalDateTime holidayStart) {
        this.holidayStart = holidayStart;
    }


    public LocalDateTime getHolidayEnd() {
        return holidayEnd;
    }


    public void setHolidayEnd(LocalDateTime holidayEnd) {
        this.holidayEnd = holidayEnd;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(id, holiday.id) &&
               Objects.equals(trainer, holiday.trainer) &&
               Objects.equals(title, holiday.title) &&
               Objects.equals(description, holiday.description) &&
               Objects.equals(holidayStart, holiday.holidayStart) &&
               Objects.equals(holidayEnd, holiday.holidayEnd);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, trainer, title, description, holidayStart, holidayEnd);
    }


    @Override
    public String toString() {
        return "Holiday{" +
               "id=" + id +
               ", trainerid=" + trainer.getId()+
               ", trainerfirstname=" + trainer.getFirstName()+
               ", trainerlastname=" + trainer.getLastName()+
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", holidayStart=" + holidayStart +
               ", holidayEnd=" + holidayEnd +
               ", groupId=" + groupId +
               '}';
    }
}

