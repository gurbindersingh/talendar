package at.ac.tuwien.sepm.groupphase.backend.security.entity;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SecurityUserProfile implements UserDetails {

    private User user;

    public SecurityUserProfile(User user) {
        this.user = user;
    }

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
