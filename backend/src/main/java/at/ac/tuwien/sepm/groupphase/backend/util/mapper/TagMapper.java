package at.ac.tuwien.sepm.groupphase.backend.util.mapper;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag tagDtoToTagEntity(TagDto tagDto);

    TagDto tagEntityToTagDto(Tag tag);

    List<TagDto> tagListToTagDtoList(List<Tag> tags);
}
