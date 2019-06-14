package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class TagDto {
    private String tag;
    private Long id;

    public TagDto(){

    }

    public TagDto(String tag, Long id) {
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
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(tag, tagDto.tag) &&
               Objects.equals(id, tagDto.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(tag, id);
    }


    @Override
    public String toString() {
        return "TagDto{" +
               "tag='" + tag + '\'' +
               ", id=" + id +
               '}';
    }
}
