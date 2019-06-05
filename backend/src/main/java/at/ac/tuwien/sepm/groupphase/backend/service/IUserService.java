package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.AccountCreationException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;

import java.util.List;

public interface IUserService {

    User createUser(User user) throws AccountCreationException;

    List<User> getAllUsers() throws ServiceException;

}
