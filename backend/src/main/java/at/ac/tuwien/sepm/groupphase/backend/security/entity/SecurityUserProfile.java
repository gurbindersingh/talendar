package at.ac.tuwien.sepm.groupphase.backend.security.entity;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper for Spring authorization related actions.
 */

public class SecurityUserProfile implements UserDetails {

    /**
     * NOTE: why not use directly Trainer as wrapped entity?
     * theoretically possible,  but a) we would expose a lot more details to spring security than
     * needed, but most importantly b) if we want to add new types of accounts (lets say customers get an account)
     * then we dont have to change a single LOC in all security related classes.
     * We would simply create a User entity somewhere else (including the meta data that we want to have set)
     * and everything would work the same way as before
     */

    // we wrap it
    private User user;

    public SecurityUserProfile(User user) {
        this.user = user;
    }


    /**
     * Set the authorities dependant of the properties of the user account.
     *
     * @return  list of granted authorities is returned.
     *          Based on this list, spring will encode this roles into the signed JWT token.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new LinkedList<>();

        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        if (user.getTrainer() != null) {
            authorities.add(new SimpleGrantedAuthority("TRAINER"));
        }

        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return !user.isDeleted();
    }

    @Override
    public boolean isEnabled() {
        return !user.isDeleted();
    }


    @Override
    public boolean isAccountNonLocked() {
        // currently we have no mechanism to lock accounts (also not needed)
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        // login credentials do not expire
        return true;
    }
}
