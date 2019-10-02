package at.ac.tuwien.sepm.groupphase.backend.rest;


import at.ac.tuwien.sepm.groupphase.backend.rest.dto.BirthdayTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IBirthdayTypeService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.BirthdayTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/talendar/birthdayTypes")
@CrossOrigin(origins = "http://localhost:4200")
public class BirthdayTypeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayTypeEndpoint.class);
    private final BirthdayTypeMapper birthdayTypeMapper;
    private final IBirthdayTypeService birthdayTypeService;

    @Autowired
    public BirthdayTypeEndpoint(
        BirthdayTypeMapper birthdayTypeMapper,
        IBirthdayTypeService birthdayTypeService
    ) {
        this.birthdayTypeMapper = birthdayTypeMapper;
        this.birthdayTypeService = birthdayTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BirthdayTypeDto save(@RequestBody BirthdayTypeDto birthdayTypeDto) throws ValidationException {
        LOGGER.info("Neue Geburtstags Typ kommt: " + birthdayTypeDto);
        try {
            return birthdayTypeMapper.birthdayEntityToDto(this.birthdayTypeService.save(birthdayTypeMapper.birthdayDtoToEntity(birthdayTypeDto)));
        }catch(ValidationException e){
            LOGGER.error("Speichern von der BirthdayType " + birthdayTypeDto + " war ungültig: " + e.getMessage());
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id){
        this.birthdayTypeService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BirthdayTypeDto> fetchAll(){
        return birthdayTypeMapper.birthdayEntityListToDtoList(this.birthdayTypeService.fetchAll());
    }

}
