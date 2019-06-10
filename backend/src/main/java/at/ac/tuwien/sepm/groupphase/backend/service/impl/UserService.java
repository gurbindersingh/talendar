package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.UserAccountConfigurationProperties;
import at.ac.tuwien.sepm.groupphase.backend.persistence.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IUserService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.AccountCreationException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserAccountConfigurationProperties userAccountConfigurationProperties;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserAccountConfigurationProperties userAccountConfigurationProperties) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userAccountConfigurationProperties = userAccountConfigurationProperties;
    }

    @Override
    public User createUser(User user
    ) throws AccountCreationException {
        LOGGER.info("Prepare to create a new user profile");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            return userRepository.save(user);
        } catch(DataAccessException e) {
            throw new AccountCreationException("User Profil konnte nicht angelegt werden", e);
        }
    }


    @Override
    public List<User> getAllUsers() throws ServiceException {
        LOGGER.info("Try to retrieve list off active users");

        try {
            return this.userRepository.findByDeletedFalse();
        } catch(DataAccessException e) {
            throw new ServiceException("User Profile konnten nicht geladen werden",  e);
        }
    }

    @PostConstruct
    public void initializeBaseAccount()  {
        // the mail is the username -- retrieve pre configured instance
        LOGGER.info("Check if admin account is set upon boot");

        String adminMail = userAccountConfigurationProperties.getEmail();
        String adminPassword = userAccountConfigurationProperties.getPassword();
        User admin;

        admin = userRepository.findByEmail(adminMail);

        if (admin == null) {
            admin = new User();
            admin.setEmail(adminMail);
            admin.setPassword(adminPassword);
            admin.setAdmin(true);
            admin.setDeleted(false);
            // currently for admin account no trainer account is associated, but can be changed on the fly
            // admin.setTrainer(whatever);
            try {
                createUser(admin);
                LOGGER.info("Admin account dod not exist upon boot. New account was created with given credentials");
            }
            catch(AccountCreationException e) {
                LOGGER.error("Admin did not exist upon boot and could not be created. Shutdown!");
                throw new IllegalStateException();
            }
        }
    }
}
