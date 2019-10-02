package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.persistence.BirthdayTypeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IBirthdayTypeService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BirthdayTypeService implements IBirthdayTypeService {

    private final static Logger LOGGER =  LoggerFactory.getLogger(BirthdayTypeService.class);
    private final BirthdayTypeRepository birthdayTypeRepository;
    private final Validator validator;



    @Autowired
    public BirthdayTypeService(
        BirthdayTypeRepository birthdayTypeRepository,
        Validator validator
    ) {
        this.birthdayTypeRepository = birthdayTypeRepository;
        this.validator = validator;
    }


    @Override
    public BirthdayType save(BirthdayType birthdayType) throws ValidationException{
        LOGGER.info("BirthdayType wird gespeichert: " + birthdayType.toString());
        Optional<BirthdayType> searched = birthdayTypeRepository.findByName(birthdayType.getName());

        if (searched.isPresent()) {
            throw new ValidationException("Ein Keyword mit dem Titel '" + birthdayType.getName() + "' existiert bereits");
        }

        try {
            validator.validateBirthdayType(birthdayType);
        }catch(InvalidEntityException e){
            throw new ValidationException(e.getMessage(), e);
        }
        return birthdayTypeRepository.save(birthdayType);
    }

    @Override
    public List<BirthdayType> fetchAll(){
        LOGGER.info("Geburtstagstypen werden geholt");
        return birthdayTypeRepository.findAll();
    }

    @Override
    public void delete(Long id){
        LOGGER.info("Geburtstagstyp mit Id " + id + " wird gelöscht: ");
        birthdayTypeRepository.deleteById(id);
    }

    @Override
    public void checkExists(String birthdayType) throws InvalidEntityException{
        Optional<BirthdayType> searched = birthdayTypeRepository.findByName(birthdayType);

        if (searched.isEmpty()) {
           throw new InvalidEntityException("This Birthday does not exist in the database");
        }
    }

    @Override
    public double getPrice(String birthdayType) throws InvalidEntityException{
        Optional<BirthdayType> searched = birthdayTypeRepository.findByName(birthdayType);

        if (searched.isEmpty()) {
            throw new InvalidEntityException("This Birthday does not exist in the database");
        }else{
            return searched.get().getPrice();
        }
    }
}
