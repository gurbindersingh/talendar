package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.entity.SecurityUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * This service simply has to load a given entity by its username.
 * In our scenario the username is an email!
 *
 * Note that all user accounts are set when a trainer is posted (these have their profile set to NonAdmin)
 * and at least one Admin account exists too (currently it is created in the UserService, see there)
 *
 *
 * Only interesting thing about this class:
 * Spring and its SecurityProviders (like the DaoAuthorityProvider) can not work with a basic entity
 * like User.
 * Therefore user is wrapped in a UserDetails object (SecurityUserProfile). This class provides all the callbacks
 * that spring needs for the automatic authentication process. (Among others, there is a callback for the
 * associated roles of a user account, see SecurityUserProfile for details)
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String email
    ) throws UsernameNotFoundException {
        User user;

        try {
            user = trainerRepository.findByEmail(email);
        } catch(DataAccessException e) {
            throw new UsernameNotFoundException("User with email " + email + " could not be loaded", e);
        }

        if (user == null) {
            throw new UsernameNotFoundException("A user with email " + email + " does not exist");
        }


        return new SecurityUserProfile(user);
    }
}
