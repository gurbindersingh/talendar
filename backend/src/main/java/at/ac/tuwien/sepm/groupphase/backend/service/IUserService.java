package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.AccountCreationException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;

import java.util.List;

public interface IUserService {

    /**
     * Create a new user (account) for an authenticated user of the system.
     * This user profile will be used for any spring security related retrieval of known users.
     * I.e. Any authentication (username/password mapping upon a login) will be matched against
     * user entities that are created by this service!
     *
     * @param user the user account for a given persona (includes credentials and account meta information)
     * @return the persisted user account
     * @throws AccountCreationException will be thrown if the user account could not be created.
     */
    User createUser(User user) throws AccountCreationException;

    /**
     * Lists all user (accounts) that exist.
     *
     * @return list of all user accounts
     * @throws ServiceException will be thrown is something goes wrong during data access (i.e.
     *                          data could not be retrieved)
     */
    List<User> getAllUsers() throws ServiceException;

}
