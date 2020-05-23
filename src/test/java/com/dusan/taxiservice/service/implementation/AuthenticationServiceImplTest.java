package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dto.request.LoginRequest;
import com.dusan.taxiservice.security.JwtToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtToken token;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testAuthenticateShouldBeSuccessful(){
        // given
        LoginRequest testReqeust = getLoginRequestModel();
        Authentication mockedAuth = Mockito.mock(Authentication.class);
        given(authManager.authenticate(any())).willReturn(mockedAuth);
        given(token.createToken(any())).willReturn("jsonWebToken");

        // when
        String createdToken = authenticationService.authenticate(testReqeust);

        // then
        assertEquals("Bearer jsonWebToken", createdToken);
    }

    @Test
    void testAuthenticateBadCreadentials(){
        // given
        LoginRequest testRequest = getLoginRequestModel();
        given(authManager.authenticate(any())).willThrow(BadCredentialsException.class);

        // when
        Executable attemptAuthentication = () -> authenticationService.authenticate(testRequest);

        // then
        assertThrows(AuthenticationException.class, attemptAuthentication);
    }

    private LoginRequest getLoginRequestModel(){
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("password");
        return request;
    }
}