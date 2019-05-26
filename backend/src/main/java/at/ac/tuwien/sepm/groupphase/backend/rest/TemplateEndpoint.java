package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TemplateDto;
import at.ac.tuwien.sepm.groupphase.backend.service.ITemplateService;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.TemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/talendar/template")
public class TemplateEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(TemplateEndpoint.class);

    private final ITemplateService templateService;

    @Autowired
    public TemplateEndpoint (ITemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public TemplateDto createNewTrainer(@RequestBody TemplateDto trainerDto) {
        return null;
    }
}
