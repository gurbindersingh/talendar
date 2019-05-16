package at.ac.tuwien.sepm.groupphase.backend.rest.dto;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.EventType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CourseDto extends EventDto {

    private LocalDateTime endOfApplication;
    private Double price;
    private Integer maxParticipants;
    private String description;
    private TrainerDto trainerDto;
    private List<CustomerDto> customerDto;


    public CourseDto () {
    }


    public CourseDto (Integer id, EventType eventType, String name, List<RoomUseDto> roomUses, LocalDateTime created, LocalDateTime updated, LocalDateTime endOfApplication, Double price, Integer maxParticipants, String description, TrainerDto trainerDto, List<CustomerDto> customerDto) {
        super(id, eventType, name, roomUses, created, updated);
        this.endOfApplication = endOfApplication;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.trainerDto = trainerDto;
        this.customerDto = customerDto;
    }


    public LocalDateTime getEndOfApplication () {
        return endOfApplication;
    }


    public void setEndOfApplication (LocalDateTime endOfApplication) {
        this.endOfApplication = endOfApplication;
    }


    public Double getPrice () {
        return price;
    }


    public void setPrice (Double price) {
        this.price = price;
    }


    public Integer getMaxParticipants () {
        return maxParticipants;
    }


    public void setMaxParticipants (Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public String getDescription () {
        return description;
    }


    public void setDescription (String description) {
        this.description = description;
    }


    public TrainerDto getTrainerDto () {
        return trainerDto;
    }


    public void setTrainerDto (TrainerDto trainerDto) {
        this.trainerDto = trainerDto;
    }


    public List<CustomerDto> getCustomerDto () {
        return customerDto;
    }


    public void setCustomerDto (List<CustomerDto> customerDto) {
        this.customerDto = customerDto;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        CourseDto courseDto = (CourseDto) o;
        return Objects.equals(endOfApplication, courseDto.endOfApplication) &&
            Objects.equals(price, courseDto.price) &&
            Objects.equals(maxParticipants, courseDto.maxParticipants) &&
            Objects.equals(description, courseDto.description) &&
            Objects.equals(trainerDto, courseDto.trainerDto) &&
            Objects.equals(customerDto, courseDto.customerDto);
    }


    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), endOfApplication, price, maxParticipants, description, trainerDto, customerDto);
    }


    @Override
    public String toString () {
        return "CourseDto{" +
            "endOfApplication=" + endOfApplication +
            ", price=" + price +
            ", maxParticipants=" + maxParticipants +
            ", description='" + description + '\'' +
            ", trainerDto=" + trainerDto +
            ", customerDto=" + customerDto +
            '}';
    }
}
