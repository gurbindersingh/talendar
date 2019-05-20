package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { RoomUseMapper.class, CustomerMapper.class, TrainerMapper.class })
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);


    @Mapping(target = "customers", source = "customerDtos")
    Event dtoToEventEntity(EventDto eventDto);

    @Mapping(target = "customerDtos", source = "customers")
    EventDto entityToEventDto(Event event);

}
