package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HolidayMapper {

    HolidayMapper INSTANCE = Mappers.getMapper(HolidayMapper.class);

    /**
     * Mapping methods are definded here. Automatic mapping between dto <-> entity
     */

    @Mapping(target = "trainer", source = "trainer")
    HolidayDto entityToHolidayDto(Holiday holiday);

    @Mapping(target = "trainer", source = "trainer")
    Holiday dtoToHolidayEntity(HolidayDto holidayDto);
}
