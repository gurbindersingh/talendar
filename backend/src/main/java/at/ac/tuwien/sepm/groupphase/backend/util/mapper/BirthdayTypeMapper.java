package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.BirthdayTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BirthdayTypeMapper {

    BirthdayTypeMapper INSTANCE = Mappers.getMapper(BirthdayTypeMapper.class);

    BirthdayType birthdayDtoToEntity(BirthdayTypeDto birthdayTypeDto);

    BirthdayTypeDto birthdayEntityToDto(BirthdayType birthdayType);

    List<BirthdayTypeDto> birthdayEntityListToDtoList(List<BirthdayType> birthdayTypes);
}
