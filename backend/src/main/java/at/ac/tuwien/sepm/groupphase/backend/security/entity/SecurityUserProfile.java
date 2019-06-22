package at.ac.tuwien.sepm.groupphase.backend.security.entity;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
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

    // actual instance of user is wrapped
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
        if (user instanceof Trainer) {
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
