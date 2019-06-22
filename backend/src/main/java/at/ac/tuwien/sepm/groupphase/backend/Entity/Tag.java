package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Tag {

    @Column
    @NotNull
    private String tag;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Tag(){

    }

    public Tag(String tag, Long id) {
        this.tag = tag;
        this.id = id;
    }


    public String getTag() {
        return tag;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Tag{" +
               "tag='" + tag + '\'' +
               ", id=" + id +
               '}';
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(tag, tag1.tag) &&
               Objects.equals(id, tag1.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(tag, id);
    }
}
