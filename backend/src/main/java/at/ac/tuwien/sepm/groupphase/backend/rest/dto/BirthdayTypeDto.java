package at.ac.tuwien.sepm.groupphase.backend.rest.dto;

import java.util.Objects;

public class BirthdayTypeDto {

    private Long id;
    private String name;
    private double price;

    public BirthdayTypeDto(){

    }

    public BirthdayTypeDto(Long id,String name,
                        double price
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
        BirthdayTypeDto that = (BirthdayTypeDto) o;
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
        return "BirthdayTypeDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
