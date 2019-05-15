package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Birthday;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.BirthdayDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { TrainerMapper.class })
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "trainer", source = "trainerDto")
    Birthday dtoToEventEntity(BirthdayDto birthdayDto);

    @Mapping(target = "eventType", constant = "Birthday")
    @Mapping(target = "trainerDto", source = "trainer")
    BirthdayDto entityToEventDto(Birthday birthday);
}
