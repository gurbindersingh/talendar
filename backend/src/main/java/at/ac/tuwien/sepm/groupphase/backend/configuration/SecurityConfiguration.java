package at.ac.tuwien.sepm.groupphase.backend.configuration;

import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.H2ConsoleConfigurationProperties;
import at.ac.tuwien.sepm.groupphase.backend.security.CustomHeaderTokenAuthenticationFilter;
import at.ac.tuwien.sepm.groupphase.backend.security.HeaderTokenAuthenticationProvider;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {


    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes((WebRequest) requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final String h2ConsolePath;
        private final String h2AccessMatcher;


        @Autowired
        private CustomUserDetailsService userDetailsService;
        @Autowired
        private HeaderTokenAuthenticationProvider authenticationProvider;

        private final PasswordEncoder passwordEncoder;


        public WebSecurityConfiguration(
            H2ConsoleConfigurationProperties h2ConsoleConfigurationProperties,
            PasswordEncoder passwordEncoder
        ) {
            h2ConsolePath = h2ConsoleConfigurationProperties.getPath();
            h2AccessMatcher = h2ConsoleConfigurationProperties.getAccessMatcher();
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((req, res, aE) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                .authorizeRequests()
                // any delete op is only accesible for the admin
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                // next two has to be allowed in order to have a free accessible authentication endpoint
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/authentication").permitAll()
                // now disable a couple of manipulating ops for normal users
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/events").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.PUT, "/api/v1/talendar/events").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/holiday").hasRole("TRAINER")
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/holidays").hasRole("TRAINER")
                .antMatchers(HttpMethod.PUT, "/api/v1/talendar/trainers").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/trainers").hasRole("ADMIN")
                /**
                 * TODO:    consider securing the get Trainers/Events Access Point.
                 *          even if we disallow showAllTrainer/Events page in frontend
                 *          data is still exposable over the REST API...!!!
                 */
                // explicitly allow SWAGGER
                .antMatchers(HttpMethod.GET,
                    //"/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-ui.html")
                .permitAll()
                // allow anything that has not been disabled by a more specific rule
                .antMatchers(HttpMethod.GET, "/**").permitAll()
            ;
            if (h2ConsolePath != null && h2AccessMatcher != null) {
                http
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**").access(h2AccessMatcher);
            }
            http
                .authorizeRequests()
                .anyRequest().authenticated() //fullyAuthenticated()
                .and()
                //.addFilterBefore(new HeaderTokenAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
                .addFilterBefore(new CustomHeaderTokenAuthenticationFilter(authenticationProvider), UsernamePasswordAuthenticationFilter.class);
        }


       @Override
       protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
       }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
             return super.authenticationManagerBean();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            return authenticationProvider;
        }

        @Bean
        public static PasswordEncoder configureDefaultPasswordEncoder() {
            return new BCryptPasswordEncoder(10);
        }

    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("PUT","POST","OPTION","GET");
        }
    }

}
