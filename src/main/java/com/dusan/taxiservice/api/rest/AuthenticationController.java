package com.dusan.taxiservice.api.rest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.dto.request.LoginRequest;
import com.dusan.taxiservice.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Authentication")
@RestController
@RequestMapping(Mappings.AUTHENTICATION_BASE_PATH)
@AllArgsConstructor
public class AuthenticationController {

    private static final String AUTHORIZATION_HEADER = "Authorization";  
    private AuthenticationService authService;
    
    @Operation(summary = "Authenticate user", description = Descriptions.AUTHENTICATE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful authentication"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid"),
                         @ApiResponse(responseCode = "401", description = "Invalid username or password")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void authenticate(
            @Valid @RequestBody LoginRequest loginRequest, 
            HttpServletResponse response) {
        String token = authService.authenticate(loginRequest);
        response.setHeader(AUTHORIZATION_HEADER, token);
    }
}
