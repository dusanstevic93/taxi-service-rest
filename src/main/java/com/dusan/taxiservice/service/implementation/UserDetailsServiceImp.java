package com.dusan.taxiservice.service.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.UserRepository;
import com.dusan.taxiservice.entity.projection.LoginProjection;
import com.dusan.taxiservice.security.UserDetailsImp;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginProjection loginProjection = userRepository.getLoginProjection(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImp(loginProjection);
    }
}
