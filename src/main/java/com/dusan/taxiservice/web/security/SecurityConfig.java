package com.dusan.taxiservice.web.security;

import com.dusan.taxiservice.core.dao.repository.UserRepository;
import com.dusan.taxiservice.web.api.rest.controller.Mappings;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        Mappings.VEHICLE_BASE_PATH + "/**",
                        Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_DRIVER,
                        Mappings.DRIVER_BASE_PATH,
                        Mappings.RETRIEVE_ALL_RIDES
                ).hasRole("DISPATCHER")
                .antMatchers(
                        Mappings.DRIVER_BASE_PATH + "/**",
                        Mappings.RETRIEVE_ALL_CREATED_RIDES
                ).hasRole("DRIVER")
                .antMatchers(
                        Mappings.CREATE_RIDE,
                        Mappings.UPDATE_RIDE,
                        Mappings.CANCEL_RIDE,
                        Mappings.RATE_RIDE,
                        Mappings.RIDE_COMMENT
                ).hasRole("CLIENT")
                .antMatchers(Mappings.RIDE_REPORT, "/socket").hasAnyRole("DRIVER, DISPATCHER")
                .antMatchers(
                        Mappings.PROFILE_BASE_PATH,
                        Mappings.RETRIEVE_USER_RIDES
                ).authenticated()
                .antMatchers(
                        Mappings.AUTHENTICATION_BASE_PATH,
                        Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_CLIENT
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
