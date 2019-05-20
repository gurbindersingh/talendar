package at.ac.tuwien.sepm.groupphase.backend.TestObjects;

import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BirthdayDto extends EventDto {


    private Integer headCount;
    private CustomerDto guardian;
    private Integer avgAge;
    private TrainerDto trainerDto;
    private BirthdayType type;


    public BirthdayDto(){

    }
    public BirthdayDto (Long id, String name, List<RoomUse> roomUses, LocalDateTime created, LocalDateTime updated, Integer headCount, CustomerDto guardian, Integer avgAge, TrainerDto trainerDto, BirthdayType type) {
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


    public CustomerDto getGuardian () {
        return guardian;
    }


    public void setGuardian (CustomerDto guardian) {
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
