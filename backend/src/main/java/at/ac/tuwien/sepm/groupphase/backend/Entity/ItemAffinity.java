package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ItemAffinity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    private String item1;
    @Column
    @NotNull
    private String item2;
    @Column
    @NotNull
    private double support;


    public ItemAffinity() {

    }
    public ItemAffinity(Long id, String item1, String item2, double support) {
        this.id = id;
        this.item1 = item1;
        this.item2 = item2;
        this.support = support;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getItem1() {
        return item1;
    }


    public void setItem1(String item1) {
        this.item1 = item1;
    }


    public String getItem2() {
        return item2;
    }


    public void setItem2(String item2) {
        this.item2 = item2;
    }


    public double getSupport() {
        return support;
    }


    public void setSupport(double support) {
        this.support = support;
    }
}
