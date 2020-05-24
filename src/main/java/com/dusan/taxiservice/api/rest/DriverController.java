package com.dusan.taxiservice.api.rest;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.api.docs.OpenApiConfig;
import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.request.DriverQueryParams;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.DriverResponse;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.service.DriverService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Driver")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@RequestMapping(Mappings.DRIVER_BASE_PATH)
@AllArgsConstructor
public class DriverController {

    private DriverService driverService;
    
    @Operation(summary = "Retrieve drivers", description = Descriptions.RETRIEVE_DRIVERS,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DriverResponse> findAllDrivers(
            @ParameterObject DriverQueryParams queryParams, 
            @Valid @ParameterObject PageParams pageParams){
        return driverService.findAllDrivers(queryParams, pageParams);
    }
       
    @Operation(summary = "Update location", description = Descriptions.UPDATE_LOCATION,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid")})
    @PutMapping(value = Mappings.UPDATE_LOCATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateLocation(
            @Valid @RequestBody LocationDto location, 
            Authentication auth) {
        driverService.updateLocation(auth.getName(), location);
    }
    
    @Operation(summary = "Set waiting for ride status", description = Descriptions.SET_WAITING_STATUS,
            responses = @ApiResponse(responseCode = "200", description = "Successful operation"))
    @PutMapping(Mappings.SET_WAITING_STATUS)
    public void setWaitingStatus(Authentication auth) {
        driverService.updateStatus(auth.getName(), DriverStatuses.WAITING_FOR_RIDE);
    }
    
    @Operation(summary = "Set not working status", description = Descriptions.SET_NOT_WORKING_STATUS,
            responses = @ApiResponse(responseCode = "200", description = "Successful operation"))
    @PutMapping(Mappings.SET_NOT_WORKING_STATUS)
    public void setNotWorkingStatus(Authentication auth) {
        driverService.updateStatus(auth.getName(), DriverStatuses.NOT_WORKING);
    }
    
    @Operation(summary = "Get status", description = Descriptions.CURRENT_STATUS)
    @GetMapping(value = Mappings.CURRENT_STATUS, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getCurrentStatus(Authentication auth) {
        return driverService.getCurrentStatus(auth.getName()).toString();
    }
}
