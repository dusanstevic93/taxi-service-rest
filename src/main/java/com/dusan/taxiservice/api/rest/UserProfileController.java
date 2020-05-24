package com.dusan.taxiservice.api.rest;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.api.docs.OpenApiConfig;
import com.dusan.taxiservice.dto.request.UpdateUserProfileRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.service.UserProfileService;

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

    private UserProfileService profileService;
    
    @Operation(summary = "Retrieve profile info", description = Descriptions.GET_USER_PROFILE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileResponse getProfile(Authentication auth) {
        return profileService.getProfile(auth.getName());
    }
    
    @Operation(summary = "Update profile info", description = Descriptions.UPDATE_USER_PROFILE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "409", description = "Email is taken")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateProfile(
                              @Valid @RequestBody UpdateUserProfileRequest updateProfileRequest, 
                              Authentication auth) {    
        profileService.updateProfile(auth.getName(), updateProfileRequest);
    }
}
