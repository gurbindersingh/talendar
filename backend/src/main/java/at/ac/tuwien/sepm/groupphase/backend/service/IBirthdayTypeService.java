package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public interface IBirthdayTypeService {

    BirthdayType save(BirthdayType birthdayType) throws ValidationException;

    List<BirthdayType> fetchAll();

    void delete(Long id);

    void checkExists(String birthdayType) throws InvalidEntityException;

    double getPrice(String birthdayType) throws InvalidEntityException;
}
