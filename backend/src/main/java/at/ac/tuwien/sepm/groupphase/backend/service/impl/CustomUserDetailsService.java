package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.persistence.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.entity.SecurityUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email
    ) throws UsernameNotFoundException {
        User user;

        try {
            user = userRepository.findByEmail(email);
        } catch(DataAccessException e) {
            throw new UsernameNotFoundException("User with email " + email + " could not be loaded", e);
        }

        if (user == null) {
            throw new UsernameNotFoundException("A user with email " + email + " does not exist");
        }


        return new SecurityUserProfile(user);
    }
}
