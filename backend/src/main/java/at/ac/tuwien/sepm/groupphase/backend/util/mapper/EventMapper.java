package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.BirthdayDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { TrainerMapper.class, CustomerMapper.class, RoomUseMapper.class })
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    default Event dtoToEntity(EventDto dto){
        if(dto == null){
            return null;
        }else if(dto instanceof BirthdayDto){
            return dtoToEventEntity((BirthdayDto) dto);
        }//TODO: Fill in the rest of the Event Subtypes
        return null;
    }

    default EventDto entityToDto(Event entity){
        if(entity == null){
            return null;
        }else if(entity instanceof Birthday){
            return entityToEventDto((Birthday) entity);
        }//TODO: Fill in the rest of the Event Subtypes
        return null;
    }

    @Mapping(target = "trainer", source = "trainerDto")
    Birthday dtoToEventEntity(BirthdayDto birthdayDto);

    @Mapping(target = "eventType", constant = "Birthday")
    @Mapping(target = "trainerDto", source = "trainer")
    BirthdayDto entityToEventDto(Birthday birthday);

}
