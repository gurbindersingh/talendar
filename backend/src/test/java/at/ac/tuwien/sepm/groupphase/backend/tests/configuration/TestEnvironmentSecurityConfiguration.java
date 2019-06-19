package at.ac.tuwien.sepm.groupphase.backend.tests.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *  ####################################################
 *  #               ATTENTION                          #
 *  ####################################################
 *      This class has only one purpose:
 *      It is meant to be used as a fallback security
 *      configuration EXCLUSIVELY!!!! for Testing.
 *
 *      This Security Configuration disables ANY
 *      authentication for the profile 'test'
 *
 *      'test' is only used as ActiveProfile by Test-
 *      classes
 *      ------------------------------------------------
 *
 *      reason why, you ask?
 *
 *      Adding Authentication is nice, but had some side-
 *      effects on the existing test suite.
 *      All tests failed (authentication failed 401)
 *      This is obvious, as any integration test uses the
 *      a simple HTTP Client Proxy to send DTOs to the
 *      controller classes, but no token are set in this
 *      abbreviated testing scenario
 *
 *      Long story short:
 *      There should be workarounds for this security problem
 *      in the testing environments (use @WithMockUser(...))
 *      or building more complex test scenarios.
 *      Other solutions which are popular are deprecated
 *      (e.g. @AutoConfigureMockMvc(secure = false))
 *
 *      All of this attempts failed, that's why we exclude
 *      security authentication for the moment!!!
 *
 *
 *
 *      LAST NODE:
 *      It is not required to use this restricted security
 *      environment. Just omit @ActiveProfile("test")
 *      on the test class!
 *      E.g. if you want to test the authentication validity
 *      itself, it is possible (just have to write a test
 *      where the authentication properties are set!)
 *
 *
 *  ****************************************************
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Profile("test")
public class TestEnvironmentSecurityConfiguration {


    // just an offset to prevent declaring the same priority twice
    private static final int PREVENT_COLLISION = 1;


    /**
     * NOTE: BASIC_AUTH_ORDER + PREVENT_COLLISION because BASIC_AUTH_ORDER was already used once
     * by the default security configuration, and same declaration is not allowed
     *
     * Therefore offset is added, to keep it running.
     *
     * NOTE 2.0: probably Order is not necessary at all, it is functioning without it but I am unsure
     * if there are any nasty side effects, therefore I use nearly the same order priority!
     */
    @Configuration
    @Profile("test")
    @Order(SecurityProperties.BASIC_AUTH_ORDER + PREVENT_COLLISION)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        public WebSecurityConfiguration() {
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                .anyRequest().permitAll();
        }
    }
}
