package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);

    /**
     *  Methods specified like this can be used to switch between two instances automatically
     *
     * see http://mapstruct.org/
     */

    TemplateDto entityToDto();

}
