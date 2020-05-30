package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.Models;
import com.dusan.taxiservice.dto.request.UpdateUserProfileRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UserProfileController.class)
class UserProfileControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserProfileService profileService;

    @Test
    @WithMockUser
    void testGetProfile() throws Exception {
        UserProfileResponse profileData = Models.getUserProfileResponseModel();

        given(profileService.getProfile(anyString())).willReturn(profileData);

        mvc.perform(get(Mappings.PROFILE_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(profileData.getUsername()))
                .andExpect(jsonPath("$.firstName").value(profileData.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(profileData.getLastName()))
                .andExpect(jsonPath("$.gender").value(profileData.getGender().name()))
                .andExpect(jsonPath("$.phone").value(profileData.getPhone()))
                .andExpect(jsonPath("$.email").value(profileData.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetProfileWithAnonymousUser() throws Exception {
        mvc.perform(get(Mappings.PROFILE_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(profileService).should(never()).getProfile(anyString());
    }

    @Test
    @WithMockUser
    void testUpdateProfile() throws Exception {
        UpdateUserProfileRequest updateRequest = Models.getUpdateUserProfileRequest();
        String json = new ObjectMapper().writeValueAsString(updateRequest);

        mvc.perform(put(Mappings.PROFILE_BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        ArgumentCaptor<UpdateUserProfileRequest> argumentCaptor = ArgumentCaptor.forClass(UpdateUserProfileRequest.class);
        then(profileService).should().updateProfile(anyString(), argumentCaptor.capture());
        UpdateUserProfileRequest profileToUpdate = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(updateRequest.getFirstName(), profileToUpdate.getFirstName()),
                () -> assertEquals(updateRequest.getLastName(), profileToUpdate.getLastName()),
                () -> assertEquals(updateRequest.getGender(), profileToUpdate.getGender()),
                () -> assertEquals(updateRequest.getPhone(), profileToUpdate.getPhone()),
                () -> assertEquals(updateRequest.getEmail(), profileToUpdate.getEmail())
        );
    }

    @Test
    @WithAnonymousUser
    void testUpdateProfileWithAnonymousUser() throws Exception {
        mvc.perform(put(Mappings.PROFILE_BASE_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(profileService).should(never()).updateProfile(anyString(), any());
    }
}