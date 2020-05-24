package com.dusan.taxiservice.api.rest;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.api.docs.OpenApiConfig;
import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.service.DriverService;
import com.dusan.taxiservice.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Vehicle")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@RequestMapping(Mappings.VEHICLE_BASE_PATH)
@AllArgsConstructor
public class VehicleController {

    private VehicleService vehicleService;
    private DriverService driverService;
    
    @Operation(summary = "Create vehicle", description = Descriptions.CREATE_VEHICLE, 
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createVehicle(@Valid @RequestBody CreateVehicleRequest createVehicleRequest) {
        vehicleService.createVehicle(createVehicleRequest);
    }
        
    @Operation(summary = "Retrieve vehicles", description = Descriptions.RETRIEVE_VEHICLES)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleResponse> retrieveAllVehicles(@Valid @ParameterObject PageParams pageParams){
        return vehicleService.findAllVehicles(pageParams);
    }
    
    @Operation(summary = "Retrieve vehicle", description = Descriptions.RETRIEVE_VEHICLE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping(value = "/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleResponse retrieveVehicle(@PathVariable long vehicleId) {
        return vehicleService.findVehicle(vehicleId);
    }
    
    @Operation(summary = "Update vehicle", description = Descriptions.UPDATE_VEHICLE, 
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Not found"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid")})
    @PutMapping(value = "/{vehicleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateVehicle(
            @PathVariable long vehicleId, 
            @Valid @RequestBody CreateVehicleRequest updateRequest) {
        vehicleService.updateVehicle(vehicleId, updateRequest);
    }
    
    @Operation(summary = "Retrieve vehicle drivers", description = Descriptions.RETRIEVE_VEHICLE_DRIVERS)
    @GetMapping(value = Mappings.RETRIEVE_VEHICLE_DRIVERS)
    public List<UserProfileResponse> retrieveDriversOfVehicle(@PathVariable long vehicleId) {
        return driverService.findAllVehicleDrivers(vehicleId);
    }
}
