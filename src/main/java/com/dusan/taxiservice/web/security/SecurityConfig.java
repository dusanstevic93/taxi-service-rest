package com.dusan.taxiservice.web.security;

import com.dusan.taxiservice.core.dao.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.dusan.taxiservice.web.api.rest.controller.Mappings.*;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        VEHICLE_BASE_PATH + "/**",
                        REGISTRATION_BASE_PATH + REGISTER_DRIVER,
                        DRIVER_BASE_PATH,
                        RETRIEVE_ALL_RIDES
                ).hasRole("DISPATCHER")
                .antMatchers(
                        DRIVER_BASE_PATH + "/**",
                        RETRIEVE_ALL_CREATED_RIDES
                ).hasRole("DRIVER")
                .antMatchers(
                        CREATE_RIDE,
                        UPDATE_RIDE,
                        CANCEL_RIDE,
                        RATE_RIDE,
                        RIDE_COMMENT
                ).hasRole("CLIENT")
                .antMatchers(
                        RIDE_REPORT,
                        "/socket"
                ).hasAnyRole("DRIVER", "DISPATCHER")
                .antMatchers(
                        PROFILE_BASE_PATH,
                        RETRIEVE_USER_RIDES
                ).authenticated()
                .antMatchers(
                        AUTHENTICATION_BASE_PATH,
                        REGISTRATION_BASE_PATH + REGISTER_CLIENT
                ).permitAll()
                .and()
                .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsServiceImpl(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }
}
