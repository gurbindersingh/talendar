package at.ac.tuwien.sepm.groupphase.backend.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Any Security related configuration is specified in this class.
 *
 * Configuration is composed of:
 * - authentication (backend requests should not be public to anyone)
 * - encryption (of passwords)
 * ...
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    // TODO
    /**
     * BEWRAE: development artifact: as long as no authentication mechanism is configured, spring
     * would prohibit any request to the REST layer.
     *
     * This set up allows any request.
     */

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        public WebSecurityConfiguration() {
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                .anyRequest().permitAll();
        }
    }
}
