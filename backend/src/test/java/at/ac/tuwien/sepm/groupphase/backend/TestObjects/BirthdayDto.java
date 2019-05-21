package at.ac.tuwien.sepm.groupphase.backend.TestObjects;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.RoomUseDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TrainerDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BirthdayDto extends EventDto {


    private Integer headCount;
    private Customer guardian;
    private Integer avgAge;
    private TrainerDto trainerDto;
    private BirthdayType type;


    public BirthdayDto (){

    }
    public BirthdayDto (Integer id, String name, List<RoomUseDto> roomUses, LocalDateTime created, LocalDateTime updated, Integer headCount, Customer guardian, Integer avgAge, TrainerDto trainerDto, BirthdayType type) {
        super(id, EventType.Birthday, name, roomUses, created, updated);
        this.headCount = headCount;
        this.guardian = guardian;
        this.avgAge = avgAge;
        this.trainerDto = trainerDto;
        this.type = type;
    }


    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        BirthdayDto that = (BirthdayDto) o;
        return Objects.equals(headCount, that.headCount) &&
            Objects.equals(guardian, that.guardian) &&
            Objects.equals(avgAge, that.avgAge) &&
            Objects.equals(trainerDto, that.trainerDto) &&
            type == that.type;
    }


    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), headCount, guardian, avgAge, trainerDto, type);
    }


    public Integer getHeadCount () {
        return headCount;
    }


    public void setHeadCount (Integer headCount) {
        this.headCount = headCount;
    }


    public Customer getGuardian () {
        return guardian;
    }


    public void setGuardian (Customer guardian) {
        this.guardian = guardian;
    }


    public Integer getAvgAge () {
        return avgAge;
    }


    public void setAvgAge (Integer avgAge) {
        this.avgAge = avgAge;
    }


    public TrainerDto getTrainerDto () {
        return trainerDto;
    }


    public void setTrainerDto (TrainerDto trainerDto) {
        this.trainerDto = trainerDto;
    }


    public BirthdayType getType () {
        return type;
    }


    public void setType (BirthdayType type) {
        this.type = type;
    }


    @Override
    public String toString () {
        return "BirthdayDto{" +
            "headCount=" + headCount +
            ", guardian=" + guardian +
            ", avgAge=" + avgAge +
            ", trainerDto=" + trainerDto +
            ", type=" + type +
            "} " + super.toString();
    }
}
