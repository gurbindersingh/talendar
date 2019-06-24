package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public interface ITagService {

    /**
     * save tag
     * @param tag to save
     * @return saved tag
     * @throws ValidationException if tag is invalid
     */
    Tag save(Tag tag) throws ValidationException;

    /**
     * get all tags in database
     * @return list of all tags
     */
    List<Tag> fetchAll();

    /**
     * detelete Tag
     * @param tag to delete
     */
    void delete(Tag tag);
}
