package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.security.JwtToken;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

public abstract class BaseControllerTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtToken jwtToken;
}
