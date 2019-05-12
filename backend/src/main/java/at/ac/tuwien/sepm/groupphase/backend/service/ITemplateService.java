package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.pojo.Template;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

// If @Valid is used then class must be annotated with @Validated
@Validated
@Repository
public interface ITemplateService {

    // Valid could be used to automatically check entities if they are annotated with @NotNull etc.
    // thrown Exception is RunTime though

    void template(@Valid Template template);
}
