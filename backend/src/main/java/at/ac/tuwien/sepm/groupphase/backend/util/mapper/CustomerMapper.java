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

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "events", source = "eventDtos")
    Customer dtoToCustomerEntity(CustomerDto customerDto);

    List<EventDto> setEventToListEventDto(Set<Event> eventSet);

    Set<Event> listEventDtoToSetEvent(List<EventDto> eventDtoList);

    @Mapping(target = "eventDtos", source = "events")
    CustomerDto entityToCustomerDto(Customer customer);


}
