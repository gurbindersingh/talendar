package at.ac.tuwien.sepm.groupphase.backend.util.mapper;


import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.RoomUseDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RoomUseMapper {

    public RoomUseMapper INSTANCE = Mappers.getMapper(RoomUseMapper.class);

    public abstract RoomUse dtoToRoomUseEntity(RoomUseDto roomUseDto);

    public abstract RoomUseDto entityToRoomUseDto(RoomUse roomUse);


}
