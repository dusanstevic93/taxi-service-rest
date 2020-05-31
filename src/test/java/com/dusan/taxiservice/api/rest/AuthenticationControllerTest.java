package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.dto.request.LoginRequest;
import com.dusan.taxiservice.model.RequestModels;
import com.dusan.taxiservice.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testAuthenticateShouldBeSuccessful() throws Exception {
        given(authenticationService.authenticate(any())).willReturn("Bearer token");
        LoginRequest request = RequestModels.getLoginRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.AUTHENTICATION_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(header().string("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticateBadCredentials() throws Exception {
        given(authenticationService.authenticate(any())).willThrow(BadCredentialsException.class);
        LoginRequest request = RequestModels.getLoginRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.AUTHENTICATION_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(status().isUnauthorized());
    }
}