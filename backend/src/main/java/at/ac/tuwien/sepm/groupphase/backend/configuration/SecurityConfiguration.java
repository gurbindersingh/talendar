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
import org.springframework.context.annotation.Profile;
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

/**
 * Basis Information about security in this context:
 *
 * Essentially there are 2 parts of security which have to be performed:
 * a) Let a User login and check credentials against known user accounts
 * n) Check JWT tokens which are sent along each request and check access rights
 *    for the requested resource!
 *
 * Both parts are NOT performed by the same classes and does not happen in the same place!
 *
 * #################################################################################################
 * a)
 * What Happens:
 * 1.)  Post a trainer leads to creation of User (account) (besides that trainer entity is created too)
 *      See User class: basically credentials and meta info (e.g. isAdmin)
 * 2.)  User login -> credentials are sent (username + password)
 *
 *      Controller received request -> SimpleHeaderTokenAuthenticationService  then called to authenticate
 *      where a AuthenticationManager is used (all automatic) but it needs a provider and this provider is
 *      registered here in the configure method (we use DaoAuthenticationProvider)
 *      after authentication SimpleHeaderTokenAuthenticationService creates the token
 *      (for the intermediate step which is done by the DAOProvider see CustomUserDetailsService)
 *
 * Classes: AuthenticationEndpoint, SimpleHeaderTokenAuthenticationService, @Bean DaoAuthenticationProvider
 *          CustomUserDetailsService, User, SecurityUserProfile
 * #################################################################################################
 * b)
 * One Note at the beginning:
 * for <a> we used a Provider (DaoAuthProvider), here we will also use a Provider (just a name for anything
 * that does the actual processing) but these 2 providers have absolutely nothing in common)
 *
 * What Happens:
 * 1.)  no direct calls, but we intercept each HTTP request and retrieve encoded token.
 * 2.)  register Filter
 * 3.)  Filter does what you would expect (extract the part of the request which is actually the token)
 * 4.)  retrieved token stored in Wrapper AuthenticationHeaderToken and this is passed to our Provider
 *      which is responsible for further processing
 * 5.)  Provider returns AuthenticationHeaderToken (now not only consisting of token but also authorities)
 *      after SimpleHeaderTokenAuthenticationService had parsed the incoming JWT token (retrieve authorities etc)
 *      and validity of token had been verified.
 * 6.)  Now we have a Authorization Object with all data (AuthenticationHeaderToken is a wrapper for that)
 * 7.)  MAGIC TIME AGAIN: you ask how does knowing the encoded roles of the verified token lead to the
 *      protection of the routes?
 *      ==> in HeaderTokenAuthenticationFilter we call SecurityContextHolder.getContext().setAuthentication(authentication)
 *
 *      For the given SpringContext (thread based, i.e. each user is handled by own thread) we set temporarily
 *      a specific role as activated (i.e if authentication includes 'ADMIN' then we set the context to 'Admin')
 *
 *      e.g. After Filtering is over Context Is Set -> request is processed -> say URL is Admin restricted
 *      -> we have ADMIN set in our context -> allow request or iff not deny request
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// security currently only enabled for these profiles (not for test profile)
// production currently not a profile but will be added eventually
@Profile({"production", "development"})
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

    // security currently only enabled for these profiles (not for test profile)
    // production currently not a profile but will be added eventually
    @Configuration
    @Profile({"production", "development"})
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final String h2ConsolePath;
        private final String h2AccessMatcher;

        /**
         * This service is responsible for retrieving the user accounts from the database!
         *
         * I.e. find a requested profile by kex (here by email), then the obtained profile will
         * be matched against full credentials by the Spring Authentication Provider
         */
        @Autowired
        private CustomUserDetailsService userDetailsService;
        @Autowired
        private HeaderTokenAuthenticationProvider authenticationProvider;

        private final PasswordEncoder passwordEncoder;


        public WebSecurityConfiguration(
            H2ConsoleConfigurationProperties h2ConsoleConfigurationProperties,
            PasswordEncoder passwordEncoder
        ) {
            // maybe unnecessary code, i dont know why we need it, but it works
            // but feel free to experiment
            h2ConsolePath = h2ConsoleConfigurationProperties.getPath();
            h2AccessMatcher = h2ConsoleConfigurationProperties.getAccessMatcher();
            // this is obviously extremely important
            this.passwordEncoder = passwordEncoder;
        }


        /**
         *  WHOLE HTTP SECURITY IS CONFIGURED (allowed routes, access restrictions)
         *
         *  And very important!!!:
         *  Here everything which is magic will be configured:
         *  -) any restriction and 401 status comes from here (ok that's the obvious stuff)
         *
         *  How does a request to any endpoint (e.g. holidayEndpoint gets verified)?
         *  -)  a filter is registered!
         *      therefore any incoming HTTP request will be pre processed by the specified
         *      filter class.
         *      This filter class (and other helper classes therein) does all the verification etc.
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                /*
                 * ######################################################################################################
                 * first some basic security setting, make it safer (apply standard sec and disable common sec breaches)
                 * ######################################################################################################
                 */
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()


                /*
                 *   #########################################################################
                 *   specify the error status which is sent upon unsuccessful requests (401)
                 *   #########################################################################
                 */
                .exceptionHandling().authenticationEntryPoint((req, res, aE) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()


                /*
                 *  ###############################################################################
                 *  a set of rules that are specified for each route (what is allowed by whom...)
                 *
                 *  NOTE:   important: most specific rules must come first, at the end the least
                 *          specific rules and fallback rules (same as when specifying routes)
                 *  ###############################################################################
                 */
                .authorizeRequests()
                // any delete op is only accesible for the admin
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                // next two has to be allowed in order to have a free accessible authentication endpoint
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/authentication").permitAll()
                // now disable a couple of manipulating ops for normal users
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/events/course").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/events/consultation").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/events/rent").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/talendar/events/birthday").permitAll()
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

                // explicitly allow SWAGGER (because this is really a sage haven)
                .antMatchers(HttpMethod.GET,
                    //"/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-ui.html")
                .permitAll()

                // allow anything that has not been disabled by a more specific rule
                .antMatchers(HttpMethod.GET, "/**").permitAll()
            ;

            /*
             * ###########################################################################################
             * Specify H2 access right: no one is allowed to fuck around with the database except spring
             * and we but we are gods. (I.e each data operation is carried out by this application).
             * ###########################################################################################
             */
            if (h2ConsolePath != null && h2AccessMatcher != null) {
                http
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**").access(h2AccessMatcher);
            }

            /*
             * ###################################################################################
             * Last but not least:
             * Each request has to be authenticated (as specified above) and we delegate this
             * process to a custom filter (which is applied to ANY incoming HTTP)
             * ###################################################################################
             */
            http
                .authorizeRequests()
                .anyRequest().authenticated() //fullyAuthenticated()
                .and()
                /*
                 * NOTE:
                 * Each Operation in respect to 'Retrieve Incoming JWT, CHECK It, Check Roles, Check Access'
                 * is carried out by Filter!!!
                 */
                .addFilterBefore(new CustomHeaderTokenAuthenticationFilter(authenticationProvider), UsernamePasswordAuthenticationFilter.class);
        }


        /**
         *  The authenticationManager does a lot itself, but we need to specify which provider should
         *  be used for authentication purposes if we do not want to use the default (which we don't)
         *
         *  We use the customized DaoAuthenticationProvider (see its Bean) for the login!
         */
       @Override
       protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
       }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
             return super.authenticationManagerBean();
        }


        /**
         * We use this provider to match submitted credentials against user accounts stored in
         * our repository (see IUserService and UserRepository)
         *
         * NOTE: DaoAuthenticationProvider needs some help:
         *     -) against which entities should he perform the authentication check?
         *        -> userDetailsService specifies this (retrieve User by email from repo)
         *
         * @return Instance thereof is returned.
         */
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            return authenticationProvider;
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
