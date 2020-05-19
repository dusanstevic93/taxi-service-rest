package com.dusan.taxiservice.service.implementation;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dto.request.LoginRequest;
import com.dusan.taxiservice.security.JwtToken;
import com.dusan.taxiservice.service.AuthenticationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {

    private static final String TOKEN_PREFIX = "Bearer ";
    
    private AuthenticationManager authManager;
    private JwtToken jwt;

    @Override
    public String authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authManager.authenticate(auth);
        UserDetails userDetails = (UserDetails)authResult.getPrincipal();
        
        String token = jwt.createToken(userDetails);
        return TOKEN_PREFIX + token;
    }
    
    
}
