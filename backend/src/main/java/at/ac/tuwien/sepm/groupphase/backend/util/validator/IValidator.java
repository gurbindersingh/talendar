package at.ac.tuwien.sepm.groupphase.backend.util.validator;

import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;

public interface IValidator<E> {

    void validate(E entity) throws InvalidEntityException;
}
