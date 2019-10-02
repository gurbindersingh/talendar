package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.LinkedList;

@Mapper
public interface ConsultingTimeMapper {


    ConsultingTimeMapper INSTANCE = Mappers.getMapper(ConsultingTimeMapper.class);

    /**
     * Mapping methods are definded here. Automatic mapping between dto <-> entity
     */
    ConsultingTimeDto entityToConsultingTimeDto(ConsultingTime consultingTime);

    ConsultingTime dtoToConsultingTimeEntity(ConsultingTimeDto consultingTimeDto);

    LinkedList<ConsultingTimeDto> entityListToConsultingTimeDtoList(LinkedList<ConsultingTime> consultingTime);

    LinkedList<ConsultingTime> dtoListToConsultingTimeEntityList(LinkedList<ConsultingTimeDto> consultingTimeDto);

}
