package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Template;
import at.ac.tuwien.sepm.groupphase.backend.service.ITemplateService;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class TemplateService implements ITemplateService {
    @Override
    public void template (@Valid Template template) {

    }
}
