package com.dusan.taxiservice.web.api.rest.controller;

import javax.validation.Valid;

import com.dusan.taxiservice.core.service.UserService;
import com.dusan.taxiservice.core.service.model.CreateDriverCommand;
import com.dusan.taxiservice.core.service.model.CreateUserCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.web.api.rest.docs.Descriptions;
import com.dusan.taxiservice.web.api.rest.docs.OpenApiConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Registration")
@RestController
@RequestMapping(Mappings.REGISTRATION_BASE_PATH)
@AllArgsConstructor
public class UserRegistrationController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    
    @Operation(summary = "Create new client", description = Descriptions.CREATE_CLIENT,
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "409", description = "Username or email is taken")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = Mappings.REGISTER_CLIENT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerClient(@Valid @RequestBody CreateUserCommand createUserCommand) {
        createUserCommand.setPassword(passwordEncoder.encode(createUserCommand.getPassword()));
        userService.createClient(createUserCommand);
    }
    
    @Operation(summary = "Create new driver", description = Descriptions.CREATE_DRIVER, security = @SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME),
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Vehicle not found"),
                         @ApiResponse(responseCode = "409", description = "Username or email is taken")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = Mappings.REGISTER_DRIVER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerDriver(@Valid @RequestBody CreateDriverCommand createDriverCommand) {
        createDriverCommand.setPassword(passwordEncoder.encode(createDriverCommand.getPassword()));
        userService.createDriver(createDriverCommand);
    }
}
