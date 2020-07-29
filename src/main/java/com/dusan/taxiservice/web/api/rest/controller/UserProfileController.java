package com.dusan.taxiservice.web.api.rest.controller;

import javax.validation.Valid;

import com.dusan.taxiservice.core.service.UserService;
import com.dusan.taxiservice.core.service.model.UpdateUserProfileCommand;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import com.dusan.taxiservice.web.api.rest.model.request.UpdateUserProfileRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.web.api.rest.docs.Descriptions;
import com.dusan.taxiservice.web.api.rest.docs.OpenApiConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Profile")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@RequestMapping(Mappings.PROFILE_BASE_PATH)
@AllArgsConstructor
public class UserProfileController {

    private UserService userService;
    
    @Operation(summary = "Retrieve profile", description = Descriptions.GET_USER_PROFILE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileDto getProfile(Authentication auth) {
        return userService.getUserProfile(auth.getName());
    }
    
    @Operation(summary = "Update profile", description = Descriptions.UPDATE_USER_PROFILE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "409", description = "Email is taken")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateProfile(@Valid @RequestBody UpdateUserProfileRequest updateRequest, Authentication auth) {
        UpdateUserProfileCommand updateCommand = new UpdateUserProfileCommand();
        updateCommand.setUsername(auth.getName());
        BeanUtils.copyProperties(updateRequest, updateCommand);
        userService.updateUserProfile(updateCommand);
    }
}
