package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class TemplateDto {

    private Long ID;
    private String test;

   public TemplateDto() {

   }


    public TemplateDto (Long ID, String test) {
        this.ID = ID;
        this.test = test;
    }


    public Long getID () {
        return ID;
    }


    public void setID (Long ID) {
        this.ID = ID;
    }


    public String getTest () {
        return test;
    }


    public void setTest (String test) {
        this.test = test;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        TemplateDto that = (TemplateDto) o;
        return Objects.equals(ID, that.ID) &&
            Objects.equals(test, that.test);
    }


    @Override
    public int hashCode () {
        return Objects.hash(ID, test);
    }


    @Override
    public String toString () {
        return "TemplateDto{" +
            "ID=" + ID +
            ", test='" + test + '\'' +
            '}';
    }
}
