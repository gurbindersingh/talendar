package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.AuthenticationConfigurationProperties;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationConstants;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleHeaderTokenAuthenticationService implements HeaderTokenAuthenticationService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(SimpleHeaderTokenAuthenticationService.class);

    // an instance which handles the JWT verification process
    private final AuthenticationManager authenticationManager;

    private final TrainerRepository trainerRepository;

    private final ObjectMapper objectMapper;

    // configs of jwt parsing etc
    private final SecretKeySpec signingKey;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Duration validityDuration;
    private final Duration overlapDuration;


    public SimpleHeaderTokenAuthenticationService(
        @Lazy AuthenticationManager authenticationManager,
        AuthenticationConfigurationProperties authenticationConfigurationProperties,
        TrainerRepository trainerRepository,
        ObjectMapper objectMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.trainerRepository = trainerRepository;
        this.objectMapper = objectMapper;
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(
            authenticationConfigurationProperties.getSecret().getBytes());
        signatureAlgorithm = authenticationConfigurationProperties.getSignatureAlgorithm();
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        validityDuration = authenticationConfigurationProperties.getValidityDuration();
        overlapDuration = authenticationConfigurationProperties.getOverlapDuration();
    }


    @Override
    public AuthenticationToken authenticate(String email, CharSequence password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        Instant now = Instant.now();
        String authorities = "";
        try {
            authorities = objectMapper.writeValueAsString(authentication.getAuthorities()
                                                                        .stream()
                                                                        .map(
                                                                            GrantedAuthority::getAuthority)
                                                                        .collect(
                                                                            Collectors.toList()));
        }
        catch(JsonProcessingException e) {
            LOGGER.error("Failed to wrap authorities", e);
        }

        // load user given the email (if existent retrieve his ID)
        Trainer user = null;
        try {user = trainerRepository.findByEmail(email);}
        catch(DataAccessException e) {
            // nothing to do, authentication will simply fail
            LOGGER.error(
                "authentication failed, no user exists that is referenced by the given email");
        }
        Long userID = null;
        if(user != null) {
            userID = user.getId();
        }
        String currentToken = Jwts.builder()
                                  .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID, userID)
                                  .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL,
                                         authentication.getName()
                                  )
                                  .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY, authorities)
                                  .setIssuedAt(Date.from(now))
                                  .setNotBefore(Date.from(now))
                                  .setExpiration(Date.from(now.plus(validityDuration)))
                                  .signWith(signatureAlgorithm, signingKey)
                                  .compact();
        String futureToken = Jwts.builder()
                                 .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID, userID)
                                 .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL,
                                        authentication.getName()
                                 )
                                 .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY, authorities)
                                 .setIssuedAt(Date.from(now))
                                 .setExpiration(Date.from(now
                                                              .plus(validityDuration
                                                                        .minus(overlapDuration)
                                                                        .plus(validityDuration))))
                                 .setNotBefore(Date.from(now
                                                             .plus(validityDuration
                                                                       .minus(overlapDuration))))
                                 .signWith(signatureAlgorithm, signingKey)
                                 .compact();
        return AuthenticationToken.builder()
                                  .currentToken(currentToken)
                                  .futureToken(futureToken)
                                  .build();
    }


    /**
     * Extract all infos from JWT token.
     */
    @Override
    public AuthenticationTokenInfo authenticationTokenInfo(String headerToken) {
        final Claims claims = Jwts.parser()
                                  .setSigningKey(signingKey)
                                  .parseClaimsJws(headerToken)
                                  .getBody();
        List<String> roles = readJwtAuthorityClaims(claims);
        return AuthenticationTokenInfo.builder()
                                      .username((String) claims.get(
                                          AuthenticationConstants.JWT_CLAIM_PRINCIPAL))
                                      .roles(roles)
                                      .issuedAt(
                                          LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(),
                                                                  ZoneId.systemDefault()
                                          ))
                                      .notBefore(
                                          LocalDateTime.ofInstant(claims.getNotBefore().toInstant(),
                                                                  ZoneId.systemDefault()
                                          ))
                                      .expireAt(LocalDateTime.ofInstant(
                                          claims.getExpiration().toInstant(),
                                          ZoneId.systemDefault()
                                      ))
                                      .validityDuration(validityDuration)
                                      .overlapDuration(overlapDuration)
                                      .build();
    }


    @Override
    public AuthenticationToken renewAuthentication(String headerToken) {
        final Claims claims = Jwts.parser()
                                  .setSigningKey(signingKey)
                                  .parseClaimsJws(headerToken)
                                  .getBody();
        String futureToken = Jwts.builder()
                                 .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID,
                                        claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID)
                                 )
                                 .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL,
                                        claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL)
                                 )
                                 .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY,
                                        claims.get(AuthenticationConstants.JWT_CLAIM_AUTHORITY)
                                 )
                                 .setIssuedAt(Date.from(Instant.now()))
                                 .setExpiration(Date.from(claims.getExpiration().toInstant()
                                                                .plus(validityDuration
                                                                          .minus(overlapDuration))))
                                 .setNotBefore(Date.from(
                                     claims.getExpiration().toInstant().minus(overlapDuration)))
                                 .signWith(signatureAlgorithm, signingKey)
                                 .compact();
        return AuthenticationToken.builder()
                                  .currentToken(headerToken)
                                  .futureToken(futureToken)
                                  .build();
    }


    /**
     * Method needed by the HeaderTokenAuthenticationProvider.
     * Get Meta data of token (principal) and authorities which are encoded in token.
     */
    @Override
    public User authenticate(String headerToken) {
        try {
            final Claims claims = Jwts.parser()
                                      .setSigningKey(signingKey)
                                      .parseClaimsJws(headerToken)
                                      .getBody();

            List<String> authoritiesWrapper = readJwtAuthorityClaims(claims);
            List<SimpleGrantedAuthority> authorities = authoritiesWrapper.stream()
                                                                         .map(
                                                                             roleName -> roleName.startsWith(
                                                                                 AuthenticationConstants.ROLE_PREFIX)
                                                                                         ?
                                                                                         roleName
                                                                                         : ( AuthenticationConstants.ROLE_PREFIX +
                                                                                             roleName
                                                                                         ))
                                                                         .map(
                                                                             SimpleGrantedAuthority::new)
                                                                         .collect(
                                                                             Collectors.toList());
            return new User(
                (String) claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL),
                headerToken,
                authorities
            );
        }
        catch(ExpiredJwtException e) {
            throw new CredentialsExpiredException(e.getMessage(), e);
        }
        catch(JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }


    /**
     * Extract authorities from JWT Claim
     */
    private List<String> readJwtAuthorityClaims(Claims claims) {
        ArrayList<String> authoritiesWrapper = new ArrayList<>();
        try {
            authoritiesWrapper = objectMapper.readValue(claims.get(
                AuthenticationConstants.JWT_CLAIM_AUTHORITY, String.class),
                                                        new TypeReference<List<String>>() {
                                                        }
            );
        }
        catch(IOException e) {
            LOGGER.error("Failed to unwrap roles", e);
        }
        return authoritiesWrapper;
    }
}