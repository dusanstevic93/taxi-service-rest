package com.dusan.taxiservice.web.api.rest.controller;

import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.core.service.model.*;
import com.dusan.taxiservice.web.api.rest.model.request.CreateRideRequest;
import com.dusan.taxiservice.web.api.rest.model.response.PageResponseWrapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.dusan.taxiservice.web.api.rest.docs.Descriptions;
import com.dusan.taxiservice.web.api.rest.docs.OpenApiConfig;
import com.dusan.taxiservice.core.service.RideService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "Ride")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@AllArgsConstructor
public class RideController {

    private RideService rideService;
    //private SimpMessagingTemplate messagingTemplate;
    
    @Operation(summary = "Retrieve user rides", description = Descriptions.RETRIEVE_USER_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_USER_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseWrapper<RideDto> retrieveUserRides(
            @ParameterObject RideQueryParams queryParams,
            @ParameterObject RidePageParams pageParams,
            Authentication auth){
        String username = auth.getName();
        UserRoles role = ControllerUtils.getRole(auth.getAuthorities());
        Page<RideDto> page = rideService.findAllRidesOfSpecificUser(username, role, queryParams, pageParams);
        return ControllerUtils.createPageResponseWrapper(page);
    }
    
    @Operation(summary = "Retrieve all rides", description = Descriptions.RETRIEVE_ALL_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_ALL_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseWrapper<RideDto> retrieveAllRides(
            @ParameterObject RideQueryParams queryParams, 
            @ParameterObject RidePageParams pageParams){
        Page<RideDto> page = rideService.findAllRides(queryParams, pageParams);
        return ControllerUtils.createPageResponseWrapper(page);
    }
    
    @Operation(summary = "Retrieve created rides", description = Descriptions.RETRIEVE_CREATED_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_ALL_CREATED_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseWrapper<RideDto> retrieveAllCreatedRides(@ParameterObject RidePageParams pageParams){
        Page<RideDto> page = rideService.findAllRidesInCreatedStatus(pageParams);
        return ControllerUtils.createPageResponseWrapper(page);
    }
    
    @Operation(summary = "Create ride", description = Descriptions.CREATE_RIDE,
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "409", description = "Client has already created ride")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = Mappings.CREATE_RIDE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createRide(
            @Valid @RequestBody CreateRideRequest createRequest, Authentication auth) {
        CreateRideCommand createCommand = new CreateRideCommand();
        BeanUtils.copyProperties(createRequest, createCommand);
        createCommand.setClientUsername(auth.getName());
        RideDto createdRide = rideService.createRide(createCommand);
    }
    
    @Operation(summary = "Update ride", description = Descriptions.UPDATE_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not in created status")})
    @PutMapping(value = Mappings.UPDATE_RIDE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateRide(
            @PathVariable long rideId, 
            @Valid @RequestBody CreateRideRequest updateRideRequest,
            Authentication auth) {
        UpdateRideCommand updateCommand = new UpdateRideCommand();
        updateCommand.setClientUsername(auth.getName());
        updateCommand.setRideId(rideId);
        BeanUtils.copyProperties(updateRideRequest, updateCommand);
        rideService.updateRide(updateCommand);
    }
      
    @Operation(summary = "Cancel ride", description = Descriptions.CANCEL_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not in created status")})
    @PutMapping(value = Mappings.CANCEL_RIDE)
    public void cancelRide(@PathVariable long rideId, Authentication auth) {
        CancelRideCommand cancelCommand = new CancelRideCommand();
        cancelCommand.setClientUsername(auth.getName());
        cancelCommand.setRideId(rideId);
        rideService.cancelRide(cancelCommand);
    }
    
    @Operation(summary = "Rate ride", description = Descriptions.RATE_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is already rated")})
    @PutMapping(value = Mappings.RATE_RIDE)
    public void rateRide(
            @PathVariable long rideId, 
            @Valid @Min(1) @Max(5) @RequestParam("rating") int rating,
            Authentication auth) {
        RateRideCommand rateCommand = new RateRideCommand();
        rateCommand.setClientUsername(auth.getName());
        rateCommand.setRideId(rideId);
        rateCommand.setRating(rating);
        rideService.rateRide(rateCommand);
    }/*
    
    @Operation(summary = "Form ride", description = Descriptions.FORM_RIDE,
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Driver not found"),
                         @ApiResponse(responseCode = "409", description = "Driver is not available")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = Mappings.FORM_RIDE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void formRide(
            @RequestParam("driver-username") String driver, 
            @Valid @RequestBody FormRideRequest formRideRequest, 
            Authentication auth) {
        rideService.formRide(auth.getName(), driver, formRideRequest);
    }
    
    @Operation(summary = "Process ride", description = Descriptions.PROCESS_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Driver not found"),
                         @ApiResponse(responseCode = "409", description = "Driver is not available or ride is not in created status")})
    @PutMapping(value = Mappings.PROCESS_RIDE)
    public void processRide(
            @PathVariable long rideId, 
            @RequestParam("driver-username") String driver, 
            Authentication auth) {
        rideService.processRide(rideId, auth.getName(), driver);
    }
    // ?
    @Operation(summary = "Accept ride", description = Descriptions.ACCEPT_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not in created status")})
    @PutMapping(value = Mappings.ACCEPT_RIDE)
    public void acceptRide(
            @PathVariable long rideId,
            Authentication auth) {
        rideService.acceptRide(rideId, auth.getName());
    }
    
    @Operation(summary = "Set failed status", description = Descriptions.SET_FAILED_STATUS,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not active")})
    @PutMapping(value = Mappings.RIDE_STATUS_FAILED, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setFailedStatus(
            @PathVariable long rideId,
            @Valid @RequestBody CreateReportRequest report,
            Authentication auth) {
        rideService.setFailedStatus(rideId, auth.getName(), report);
    }
    
    @Operation(summary = "Set successful status", description = Descriptions.SET_SUCCESSFUL_STATUS,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not active")})
    @PutMapping(value = Mappings.RIDE_STATUS_SUCCESSFUL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setSuccessfulStatus(
            @PathVariable long rideId,
            @Valid @RequestBody SuccessfulRideRequest successfulRideRequest,
            Authentication auth) {
        rideService.setSuccessfulStatus(rideId, auth.getName(), successfulRideRequest);
    }*/
}
