package com.dusan.taxiservice.api.rest;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.api.docs.SecuritySchemeDefinition;
import com.dusan.taxiservice.dto.request.CreateReportRequest;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.dto.request.FormRideRequest;
import com.dusan.taxiservice.dto.request.RidePageParams;
import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.dto.request.SuccessfulRideRequest;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.service.RideService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Ride")
@SecurityRequirement(name = SecuritySchemeDefinition.BEARER_TOKEN)
@RestController
@AllArgsConstructor
public class RideController {

    private RideService rideService;
    
    @Operation(summary = "Retrieve user rides", description = Descriptions.RETRIEVE_USER_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_USER_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RideResponse> retrieveUserRides(@ParameterObject RideQueryParams queryParams, @ParameterObject RidePageParams pageParams, Authentication auth){
        String username = auth.getName();
        UserRoles role = getRole(auth.getAuthorities());
        return rideService.findAllRidesOfSpecificUser(username, role, queryParams, pageParams);
    }
    
    private UserRoles getRole(Collection<? extends GrantedAuthority> authorities) {
        String roleWithPrefix = authorities.stream()
                                .map(element -> element.getAuthority())
                                .findFirst()
                                .get();
        return UserRoles.valueOf(roleWithPrefix.replace("ROLE_", ""));   
    }
    
    @Operation(summary = "Retrieve all rides", description = Descriptions.RETRIEVE_ALL_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_ALL_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RideResponse> retrieveAllRides(@ParameterObject RideQueryParams queryParams, @ParameterObject RidePageParams pageParams){
        return rideService.findAllRides(queryParams, pageParams);
    }
    
    @Operation(summary = "Retrieve created rides", description = Descriptions.RETRIEVE_CREATED_RIDES)
    @GetMapping(value = Mappings.RETRIEVE_ALL_CREATED_RIDES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RideResponse> retrieveAllCreatedRides(@ParameterObject RidePageParams pageParams){
        return rideService.findAllRidesInCreatedStatus(pageParams);
    }
    
    @Operation(summary = "Create ride", description = Descriptions.CREATE_RIDE,
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is invalid or missing"),
                         @ApiResponse(responseCode = "409", description = "Client has unfinished ride")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = Mappings.CREATE_RIDE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createRide(
            @Valid @RequestBody CreateRideRequest createRideRequest, 
            Authentication auth) {
        rideService.createRide(auth.getName(), createRideRequest);
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
        rideService.updateRide(rideId, auth.getName(), updateRideRequest);
    }
      
    @Operation(summary = "Cancel ride", description = Descriptions.CANCEL_RIDE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Ride is not found"),
                         @ApiResponse(responseCode = "409", description = "Ride is not in created status")})
    @PutMapping(value = Mappings.CANCEL_RIDE)
    public void cancelRide(
            @PathVariable long rideId,
            Authentication auth) {
        rideService.cancelRide(rideId, auth.getName());
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
        rideService.rateRide(rideId, auth.getName(), rating);
    }
    
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
            Authentication auth
            ) {
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
            Authentication auth
            ) {
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
            Authentication auth
            ) {
        rideService.setSuccessfulStatus(rideId, auth.getName(), successfulRideRequest);
    }
}
