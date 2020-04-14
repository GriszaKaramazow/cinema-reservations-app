package pl.connectis.cinemareservationsapp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.connectis.cinemareservationsapp.model.Role;
import pl.connectis.cinemareservationsapp.repository.UserRepository;

@PropertySource("classpath:security.properties")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserPrincipalDetailsService userPrincipalDetailsService;
    private final UserRepository userRepository;
    private final String secret;
    private final int expirationTime;

    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService,
                                 UserRepository userRepository,
                                 @Value("${jwt.secret}") String secret,
                                 @Value("${jwt.expiration.time}") int expirationTime) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.userRepository = userRepository;
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), this.secret, this.expirationTime))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository, this.secret))
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/movie", "/session")
                    .hasRole(String.valueOf(Role.EMPLOYEE))
                .antMatchers(HttpMethod.PUT, "/movie", "/session")
                    .hasRole(String.valueOf(Role.EMPLOYEE))
                .antMatchers(HttpMethod.DELETE,"/movie", "/session")
                    .hasRole(String.valueOf(Role.EMPLOYEE))
                .antMatchers("/signup", "/login")
                    .anonymous()
                .antMatchers("/register", "/client", "/room", "/ticket")
                    .hasRole(String.valueOf(Role.EMPLOYEE))
                .antMatchers("/reservation", "/mytickets")
                    .hasRole(String.valueOf(Role.CLIENT))
                .antMatchers(HttpMethod.GET,"/movie/**", "/session/**")
                    .permitAll()
                .anyRequest().authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
