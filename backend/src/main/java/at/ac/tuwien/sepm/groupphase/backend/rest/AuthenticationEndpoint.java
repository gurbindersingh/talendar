package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationConstants;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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


    @RequestMapping(method = RequestMethod.POST)
    public AuthenticationToken authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {
        LOGGER.info("Incoming POST Request For Authentication");
        return authenticationService.authenticate(authenticationRequest.getEmail(),
                                                  authenticationRequest.getPassword()
        );
    }


    @RequestMapping(method = RequestMethod.GET)
    public AuthenticationToken authenticate(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.renewAuthentication(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }


    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    public AuthenticationTokenInfo tokenInfoAny(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public AuthenticationTokenInfo tokenInfoCurrent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }
}
