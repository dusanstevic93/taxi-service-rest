package com.dusan.taxiservice.web.security;

import com.dusan.taxiservice.core.dao.projection.LoginProjection;
import com.dusan.taxiservice.core.dao.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginProjection loginDetails = userRepository.getLoginProjection(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return new UserDetailsImpl(loginDetails);
    }
}
