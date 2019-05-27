package at.ac.tuwien.sepm.groupphase.backend.util.mapper;


import at.ac.tuwien.sepm.groupphase.backend.Entity.RoomUse;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.RoomUseDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomUseMapper {

    RoomUseMapper INSTANCE = Mappers.getMapper(RoomUseMapper.class);

    RoomUse dtoToRoomUseEntity(RoomUseDto roomUseDto);

    RoomUseDto entityToRoomUseDto(RoomUse roomUse);

}
