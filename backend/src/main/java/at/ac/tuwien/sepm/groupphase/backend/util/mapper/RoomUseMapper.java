package at.ac.tuwien.sepm.groupphase.backend.util.mapper;


import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.BirthdayDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.RoomUseDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RoomUseMapper {

    public RoomUseMapper INSTANCE = Mappers.getMapper(RoomUseMapper.class);

    public abstract RoomUse dtoToRoomUseEntity(RoomUseDto roomUseDto);

    @Mapping(target = "event", ignore = true)
    public abstract RoomUseDto entityToRoomUseDto(RoomUse roomUse);

    @AfterMapping
    protected void ignoreEventRooms(RoomUse entity, @MappingTarget RoomUseDto dto){
        dto.getEvent().setRoomUses(null);
    }

}
