package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.model.RequestModels;
import com.dusan.taxiservice.model.ResponseModels;
import com.dusan.taxiservice.service.DriverService;
import com.dusan.taxiservice.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private DriverService driverService;

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testCreateVehicleShouldBeSuccessful() throws Exception {
        CreateVehicleRequest request = RequestModels.getCreateVehicleRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post(Mappings.VEHICLE_BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateVehicleRequest> argumentCaptor = ArgumentCaptor.forClass(CreateVehicleRequest.class);
        then(vehicleService).should().createVehicle(argumentCaptor.capture());
        CreateVehicleRequest vehicleToCreate = argumentCaptor.getValue();
        assertAll(
                () -> assertEquals(request.getProductionYear(), vehicleToCreate.getProductionYear()),
                () -> assertEquals(request.getLicencePlate(), vehicleToCreate.getLicencePlate()),
                () -> assertEquals(request.getType(), vehicleToCreate.getType())
        );
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DRIVER"})
    void testCreateVehicleUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(post(Mappings.VEHICLE_BASE_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(vehicleService).should(never()).createVehicle(any());
    }

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testRetrieveAllVehiclesShouldBeSuccessful() throws Exception {
        List<VehicleResponse> vehicles = Stream.of(1, 2, 3)
                .map(i -> new VehicleResponse())
                .collect(Collectors.toList());
        given(vehicleService.findAllVehicles(any())).willReturn(vehicles);
        mvc.perform(get(Mappings.VEHICLE_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(vehicles.size())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DRIVER"})
    void testRetrieveAllVehiclesUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(get(Mappings.VEHICLE_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(vehicleService).should(never()).findAllVehicles(any());
    }

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testRetrieveVehicleShouldBeSuccessful() throws Exception {
        VehicleResponse response = ResponseModels.getVehicleResponseModel();
        given(vehicleService.findVehicle(response.getId())).willReturn(response);
        mvc.perform(get(Mappings.VEHICLE_BASE_PATH + "/{vehicleId}", response.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.licencePlate").value(response.getLicencePlate()))
                .andExpect(jsonPath("$.productionYear").value(response.getProductionYear()))
                .andExpect(jsonPath("$.vehicleType").value(response.getVehicleType().name()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DRIVER"})
    void testRetrieveVehicleUserDoesNOtHaveRequiredRole() throws Exception {
        mvc.perform(get(Mappings.VEHICLE_BASE_PATH + "/{vehicleId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(vehicleService).should(never()).findVehicle(anyLong());
    }

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testUpdateVehicleShouldBeSuccessful() throws Exception {
        CreateVehicleRequest request = RequestModels.getCreateVehicleRequestModel();
        String json = new ObjectMapper().writeValueAsString(request);
        mvc.perform(put(Mappings.VEHICLE_BASE_PATH + "/{vehicleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        ArgumentCaptor<CreateVehicleRequest> argumentCaptor = ArgumentCaptor.forClass(CreateVehicleRequest.class);
        then(vehicleService).should().updateVehicle(eq(1L), argumentCaptor.capture());
        CreateVehicleRequest vehicleToUpdate = argumentCaptor.getValue();
        assertAll(
                () -> assertEquals(request.getProductionYear(), vehicleToUpdate.getProductionYear()),
                () -> assertEquals(request.getLicencePlate(), vehicleToUpdate.getLicencePlate()),
                () -> assertEquals(request.getType(), vehicleToUpdate.getType())
        );
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DRIVER"})
    void testUpdateVehicleUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(put(Mappings.VEHICLE_BASE_PATH + "/{vehicleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(vehicleService).should(never()).updateVehicle(anyLong(), any());
    }

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testRetrieveDriversOfVehicleShouldBeSuccessful() throws Exception {
        List<UserProfileResponse> drivers = Stream.of(1, 2, 3).map(i -> new UserProfileResponse())
                .collect(Collectors.toList());
        given(driverService.findAllVehicleDrivers(1L)).willReturn(drivers);

        mvc.perform(get(Mappings.VEHICLE_BASE_PATH + Mappings.RETRIEVE_VEHICLE_DRIVERS, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(drivers.size())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"DRIVER", "CLIENT"})
    void testRetrieveDriversOfVehicleUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(get(Mappings.VEHICLE_BASE_PATH + Mappings.RETRIEVE_VEHICLE_DRIVERS, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).findAllVehicleDrivers(anyLong());
    }
}