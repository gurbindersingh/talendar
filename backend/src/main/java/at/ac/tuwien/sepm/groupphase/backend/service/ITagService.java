package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public interface ITagService {

    Tag save(Tag tag) throws ValidationException;

    List<Tag> fetchAll();

    void delete(Tag tag);
}
