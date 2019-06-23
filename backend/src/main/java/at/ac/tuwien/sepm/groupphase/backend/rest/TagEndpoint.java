package at.ac.tuwien.sepm.groupphase.backend.rest;


import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TagService;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/talendar/tags")
@CrossOrigin(origins = "http://localhost:4200")
public class TagEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagEndpoint.class);
    private final TagService tagService;
    private final TagMapper tagMapper;


    @Autowired
    public TagEndpoint(TagService tagService,
                       TagMapper tagMapper
    ) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto save(@RequestBody TagDto tagDto) throws ValidationException {
        try {
            return tagMapper.tagEntityToTagDto(this.tagService.save(tagMapper.tagDtoToTagEntity(tagDto)));
        }catch(ValidationException e){
            LOGGER.error("Speicher von der Tag " + tagDto + " war ungültig");
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody TagDto tagDto){
        this.tagService.delete(tagMapper.tagDtoToTagEntity(tagDto));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> fetchAll(){
        return tagMapper.tagListToTagDtoList(this.tagService.fetchAll());
    }

}
