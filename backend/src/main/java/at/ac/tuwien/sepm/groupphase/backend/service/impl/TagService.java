package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TagRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ITagService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements ITagService {

    private TagRepository tagRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(TagService.class);
    private final Validator validator;

    @Autowired
    public TagService(TagRepository tagRepository,
                      Validator validator
    ) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public Tag save(Tag tag) throws ValidationException {
        LOGGER.info("Tag wird gespeichert");

        Optional<Tag> searched = tagRepository.findByTag(tag.getTag());

        if (searched.isPresent()) {
            throw new ValidationException("Ein Keyword mit dem Titel '" + tag.getTag() + "' existiert bereits");
        }

        try {
            validator.validateTag(tag);
        }catch(InvalidEntityException e){
            throw new ValidationException(e.getMessage(), e);
        }
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> fetchAll(){
        LOGGER.info("Tags werden geholt");
        return tagRepository.findAll();
    }

    @Override
    public void delete(Tag tag){
        tagRepository.delete(tag);
    }
}

