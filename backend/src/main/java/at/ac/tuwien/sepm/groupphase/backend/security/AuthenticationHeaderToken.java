package at.ac.tuwien.sepm.groupphase.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Class that wraps an authentication which is an object created by spring after a request has been
 * processed and authenticated!
 *
 * This is like an Spring Version of a DTO. It is used to wrap an unparsed token in the beginning.
 * And it is also used to wrap the result of a parsed token (i.e. extracted roles among others)
 */

public class AuthenticationHeaderToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 739L;

    private final String token;
    private final Object principal;

    public AuthenticationHeaderToken(String token) {
        super(null);
        this.token = token;
        principal = null;
        setAuthenticated(false);
    }

    public AuthenticationHeaderToken(Object principal, String token,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
