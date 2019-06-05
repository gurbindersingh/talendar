package at.ac.tuwien.sepm.groupphase.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomHeaderTokenAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER =
        LoggerFactory.getLogger(CustomHeaderTokenAuthenticationFilter.class);

    private final HeaderTokenAuthenticationProvider authenticationProvider;


    @Autowired
    public CustomHeaderTokenAuthenticationFilter( HeaderTokenAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(( header == null ) || !header.startsWith(AuthenticationConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            AuthenticationHeaderToken authenticationRequest = new AuthenticationHeaderToken(header.substring(AuthenticationConstants.TOKEN_PREFIX.length()));

            Authentication authentication = authenticationProvider.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch(AuthenticationException failed) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
