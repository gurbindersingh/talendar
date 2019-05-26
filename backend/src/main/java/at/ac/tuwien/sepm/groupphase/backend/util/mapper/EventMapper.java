package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(uses = { RoomUseMapper.class, CustomerMapper.class, TrainerMapper.class })
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);


    @Mapping(target = "customers", source = "customerDtos")
    Event dtoToEventEntity (EventDto eventDto);

    Set<Customer> listToSet (List<CustomerDto> customerList);

    List<CustomerDto> setToList (Set<Customer> customerSet);

    @Mapping(target = "customerDtos", source = "customers")
    EventDto entityToEventDto (Event event);

    List<EventDto> entityListToEventDtoList (List<Event> eventList);

}
