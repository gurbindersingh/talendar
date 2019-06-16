package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.annotations.Anyone;
import at.ac.tuwien.sepm.groupphase.backend.annotations.IsAdmin;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication.AuthenticationRequest;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationConstants;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleHeaderTokenAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Anyone
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping(value = "/api/v1/talendar/authentication")
public class AuthenticationEndpoint {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEndpoint.class);

    private final HeaderTokenAuthenticationService authenticationService;


    public AuthenticationEndpoint(
        SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService
    ) {
        authenticationService = simpleHeaderTokenAuthenticationService;
    }


    /**
     * Endpoint for the login: if credentials can be mapped to a known user, a JWT token for
     * authentication will created and returned.
     * @param authenticationRequest the login credentials (email and password)
     * @return  a pair of 2 JWT token (current and future token) is returned upon a valid login.
     *          the period of valid authentication is split up into 2 tokens
     *          a user may decide on his own to switch to the newer token if the old expired
     */
    @RequestMapping(method = RequestMethod.POST)
    public AuthenticationToken authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {
        LOGGER.info("Incoming POST Request For Authentication");
        return authenticationService.authenticate(authenticationRequest.getEmail(),
                                                  authenticationRequest.getPassword()
        );
    }


    /**
     * NOTE:    this endpoint is currently not used by the frontend but I think it would be handy to
     *          to upgrade frontend eventually to use this endpoint.
     *          I.e. If a token is valid from t1 - t9, and a request is made at t8, then the frontend
     *          could be so smart as to request a new set of tokens for the future instead of letting
     *          their set of tokens expire....
     *
     * Endpoint for any user that already has a token after successful login but wants to renew it!
     * @param authorizationHeader given valid JWT token
     * @return  upon success (i.e. given token was valid and was not already expired) then a pair of
     *          new tokens is returned.
     */
    @RequestMapping(method = RequestMethod.GET)
    public AuthenticationToken authenticate(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.renewAuthentication(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }


    /**
     * Endpoint to return JWT internal details (i.e. meta information and user data thar are encoded
     * into the token).
     *
     * @param token given valid JWT token
     * @return  an object is returned which encapsulates JWR and user related information
     *          (e.g. issuedAt, expiration, email, roles, etc)
     */
    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    public AuthenticationTokenInfo tokenInfo(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }


    /**
     * This is basically the same endpoint as above.
     * Token is read from HTTP header which makes this endpoint more secure.
     *
     * @param authorizationHeader HTTP header that includes the JWT Token
     * @return  an object is returned which encapsulates JWR and user related information
     *          e.g. issuedAt, expiration, email, roles, etc)
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public AuthenticationTokenInfo tokenInfoCurrent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }
}
