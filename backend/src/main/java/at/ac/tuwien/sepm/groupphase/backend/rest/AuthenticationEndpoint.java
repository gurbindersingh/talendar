package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationConstants;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/authentication")
@Api(value = "authentication")
public class AuthenticationEndpoint {

    private final HeaderTokenAuthenticationService authenticationService;


    public AuthenticationEndpoint(
        SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService
    ) {
        authenticationService = simpleHeaderTokenAuthenticationService;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Get an authentication token with your username and password")
    public AuthenticationToken authenticate(
        @RequestBody final AuthenticationRequest authenticationRequest
    ) {
        return authenticationService.authenticate(authenticationRequest.getUsername(),
                                                  authenticationRequest.getPassword()
        );
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get some valid authentication tokens",
                  authorizations = { @Authorization(value = "apiKey") })
    public AuthenticationToken authenticate(
        @ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return authenticationService.renewAuthentication(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }


    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific authentication token",
                  authorizations = { @Authorization(value = "apiKey") })
    public AuthenticationTokenInfo tokenInfoAny(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about the current users authentication token",
                  authorizations = { @Authorization(value = "apiKey") })
    public AuthenticationTokenInfo tokenInfoCurrent(
        @ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return authenticationService.authenticationTokenInfo(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }
}
