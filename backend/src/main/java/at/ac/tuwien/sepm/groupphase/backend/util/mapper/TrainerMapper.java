package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TrainerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    /**
     * Mapping methods are definded here. Automatic mapping between dto <-> entity
     */


    TrainerDto entityToTrainerDto(Trainer trainer);

    Trainer dtoToTrainerEntity(TrainerDto trainerDto);
}
