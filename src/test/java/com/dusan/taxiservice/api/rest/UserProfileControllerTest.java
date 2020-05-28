package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(controllers = UserProfileController.class)
class UserProfileControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserProfileService profileService;

    @Test
    @WithMockUser
    void testGetProfile() throws Exception {
        UserProfileResponse profileData = getProfileData();

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
        String json = new ObjectMapper().writeValueAsString(getProfileData());

        mvc.perform(put(Mappings.PROFILE_BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        then(profileService).should().updateProfile(anyString(), any());
    }

    @Test
    @WithAnonymousUser
    void testUpdateProfileWithAnonymousUser() throws Exception {
        String json = new ObjectMapper().writeValueAsString(getProfileData());

        mvc.perform(put(Mappings.PROFILE_BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());

        then(profileService).should(never()).updateProfile(anyString(), any());
    }

    private UserProfileResponse getProfileData(){
        UserProfileResponse profileData = new UserProfileResponse();
        profileData.setUsername("username test");
        profileData.setFirstName("first name test");
        profileData.setLastName("last name test");
        profileData.setEmail("test@mail.com");
        profileData.setPhone("0123");
        profileData.setGender(Gender.MALE);
        return profileData;
    }
}