package com.dusan.taxiservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dusan.taxiservice.api.rest.Mappings;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class Config extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    
    private JwtToken jwtToken;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeRequests()
        .antMatchers(
                     Mappings.VEHICLE_BASE_PATH + "/**",
                     Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_DRIVER,
                     Mappings.DRIVER_BASE_PATH,
                     Mappings.RETRIEVE_ALL_RIDES,
                     Mappings.FORM_RIDE,
                     Mappings.PROCESS_RIDE
                     ).hasRole("DISPATCHER")
        .antMatchers(
                     Mappings.DRIVER_BASE_PATH + "/**",
                     Mappings.RETRIEVE_ALL_CREATED_RIDES,
                     Mappings.ACCEPT_RIDE,
                     Mappings.RIDE_STATUS_FAILED,
                     Mappings.RIDE_STATUS_SUCCESSFUL
                     ).hasRole("DRIVER")
        .antMatchers(
                     Mappings.CREATE_RIDE,
                     Mappings.UPDATE_RIDE,
                     Mappings.CANCEL_RIDE,
                     Mappings.RATE_RIDE,
                     Mappings.RIDE_COMMENT
                     ).hasRole("CLIENT")
        .antMatchers(Mappings.RIDE_REPORT).hasAnyRole("DRIVER, DISPATCHER")
        .antMatchers(
                     Mappings.PROFILE_BASE_PATH,
                     Mappings.RETRIEVE_USER_RIDES
                     ).authenticated()
        .antMatchers(
                     Mappings.AUTHENTICATION_BASE_PATH,
                     Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_CLIENT
                     ).permitAll()
        .and()
        .addFilterAfter(new AuthorizationFilter(jwtToken), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
