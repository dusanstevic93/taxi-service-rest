package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.Models;
import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.response.DriverResponse;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.service.DriverService;
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

@WebMvcTest(controllers = DriverController.class)
class DriverControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DriverService driverService;

    @Test
    @WithMockUser(roles = "DISPATCHER")
    void testFindAllDriversShouldBeSuccessful() throws Exception {
        List<DriverResponse> drivers = Stream.of(1, 2, 3)
                .map(i -> new DriverResponse())
                .collect(Collectors.toList());
        given(driverService.findAllDrivers(any(), any())).willReturn(drivers);

        mvc.perform(get(Mappings.DRIVER_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(drivers.size())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"DRIVER", "CLIENT"})
    void testFindAllDriversUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(get(Mappings.DRIVER_BASE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).findAllDrivers(any(), any());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testUpdateLocationShouldBeSuccessful() throws Exception {
        LocationDto location = Models.getLocationDtoModel();
        String json = new ObjectMapper().writeValueAsString(location);
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.UPDATE_LOCATION)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        ArgumentCaptor<LocationDto> argumentCaptor = ArgumentCaptor.forClass(LocationDto.class);
        then(driverService).should().updateLocation(any(), argumentCaptor.capture());
        LocationDto updatedLocation = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(location.getLatitude(), updatedLocation.getLatitude()),
                () -> assertEquals(location.getLongitude(), updatedLocation.getLongitude())
        );
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DISPATCHER"})
    void testUpdateLocationUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.UPDATE_LOCATION)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).updateLocation(any(), any());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testSetWaitingStatusShouldBeSuccessful() throws Exception {
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.SET_WAITING_STATUS))
                .andExpect(status().isOk());

        ArgumentCaptor<DriverStatuses> argumentCaptor = ArgumentCaptor.forClass(DriverStatuses.class);
        then(driverService).should().updateStatus(any(), argumentCaptor.capture());
        DriverStatuses status = argumentCaptor.getValue();
        assertEquals(DriverStatuses.WAITING_FOR_RIDE, status);
    }

    @Test
    @WithMockUser(roles = {"DISPATCHER", "CLIENT"})
    void testSetWaitingStatusUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.SET_WAITING_STATUS))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).updateLocation(any(), any());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testSetNotWorkingStatusShouldBeSuccessful() throws Exception {
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.SET_NOT_WORKING_STATUS))
                .andExpect(status().isOk());

        ArgumentCaptor<DriverStatuses> argumentCaptor = ArgumentCaptor.forClass(DriverStatuses.class);
        then(driverService).should().updateStatus(any(), argumentCaptor.capture());
        DriverStatuses status = argumentCaptor.getValue();
        assertEquals(DriverStatuses.NOT_WORKING, status);
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DISPATCHER"})
    void testSetNotWorkingStatusUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(put(Mappings.DRIVER_BASE_PATH + Mappings.SET_NOT_WORKING_STATUS))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).updateLocation(any(), any());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testGetCurrentStatusShouldBeSuccessful() throws Exception {
        given(driverService.getCurrentStatus(any())).willReturn(DriverStatuses.ON_RIDE);
        mvc.perform(get(Mappings.DRIVER_BASE_PATH + Mappings.CURRENT_STATUS))
                .andExpect(content().string(DriverStatuses.ON_RIDE.name()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"CLIENT", "DISPATCHER"})
    void testGetCurrentStatusUserDoesNotHaveRequiredRole() throws Exception {
        mvc.perform(get(Mappings.DRIVER_BASE_PATH + Mappings.CURRENT_STATUS))
                .andExpect(status().isForbidden());

        then(driverService).should(never()).getCurrentStatus(any());
    }
}