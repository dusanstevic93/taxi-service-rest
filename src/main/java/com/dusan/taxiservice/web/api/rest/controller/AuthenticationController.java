package com.dusan.taxiservice.web.api.rest.controller;

import javax.validation.Valid;

import com.dusan.taxiservice.web.api.rest.model.request.AuthenticationRequest;
import com.dusan.taxiservice.web.security.JwtToken;
import com.dusan.taxiservice.web.security.JwtUtil;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.web.api.rest.docs.Descriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Authentication")
@RestController
@RequestMapping(Mappings.AUTHENTICATION_BASE_PATH)
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;
    
    @Operation(summary = "Authenticate user", description = Descriptions.AUTHENTICATE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful authentication"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid", content = @Content),
                         @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtToken authenticate(@Valid @RequestBody AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication auth = authManager.authenticate(authenticationToken);
        String[] roles = auth.getAuthorities().stream()
                .map(Object::toString)
                .toArray(String[]::new);
        return jwtUtil.createToken(request.getUsername(), roles);
    }
}
