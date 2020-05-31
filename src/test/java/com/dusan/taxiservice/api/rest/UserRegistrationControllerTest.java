package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.model.RequestModels;
import com.dusan.taxiservice.service.UserRegistrationService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRegistrationController.class)
class UserRegistrationControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRegistrationService registrationService;

    @Test
    void testRegisterClient() throws Exception {
        CreateClientRequest request = RequestModels.getCreateClientRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_CLIENT)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateClientRequest> argumentCaptor = ArgumentCaptor.forClass(CreateClientRequest.class);
        then(registrationService).should().registerClient(argumentCaptor.capture());
        CreateClientRequest clientToRegister = argumentCaptor.getValue();
        assertAll(
                () -> assertEquals(request.getUsername(), clientToRegister.getUsername()),
                () -> assertEquals(request.getPassword(), clientToRegister.getPassword()),
                () -> assertEquals(request.getFirstName(), clientToRegister.getFirstName()),
                () -> assertEquals(request.getLastName(), clientToRegister.getLastName()),
                () -> assertEquals(request.getGender(), clientToRegister.getGender()),
                () -> assertEquals(request.getPhone(), clientToRegister.getPhone()),
                () -> assertEquals(request.getEmail(), clientToRegister.getEmail())
        );
    }

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testRegisterDriverShouldBeSuccessful() throws Exception {
        CreateDriverRequest request = RequestModels.getCreateDriverRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_DRIVER)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateDriverRequest> argumentCaptor = ArgumentCaptor.forClass(CreateDriverRequest.class);
        then(registrationService).should().registerDriver(argumentCaptor.capture());
        CreateDriverRequest driverToRegister = argumentCaptor.getValue();
        assertAll(
                () -> assertEquals(request.getUsername(), driverToRegister.getUsername()),
                () -> assertEquals(request.getPassword(), driverToRegister.getPassword()),
                () -> assertEquals(request.getFirstName(), driverToRegister.getFirstName()),
                () -> assertEquals(request.getLastName(), driverToRegister.getLastName()),
                () -> assertEquals(request.getGender(), driverToRegister.getGender()),
                () -> assertEquals(request.getPhone(), driverToRegister.getPhone()),
                () -> assertEquals(request.getEmail(), driverToRegister.getEmail()),
                () -> assertEquals(request.getVehicleId(), driverToRegister.getVehicleId())
        );
    }

    @Test
    @WithMockUser(roles = {"DRIVER, CLIENT"})
    void testRegisterDriverUserDoesNotHaveRequiredRole() throws Exception {
        CreateDriverRequest request = RequestModels.getCreateDriverRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_DRIVER)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());

        then(registrationService).should(never()).registerDriver(any());
    }

    @Test
    @WithAnonymousUser
    void testRegisterDriverUserIsAnonymous() throws Exception {
        CreateDriverRequest request = RequestModels.getCreateDriverRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.REGISTRATION_BASE_PATH + Mappings.REGISTER_DRIVER)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());

        then(registrationService).should(never()).registerDriver(any());
    }
}