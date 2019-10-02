package at.ac.tuwien.sepm.groupphase.backend.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class BirthdayType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    public BirthdayType(){

    }

    public BirthdayType(Long id, @NotBlank String name,
                        @NotBlank double price
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        BirthdayType that = (BirthdayType) o;
        return Double.compare(that.price, price) == 0 &&
               Objects.equals(id, that.id) &&
               Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }


    @Override
    public String toString() {
        return "BirthdayType{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
