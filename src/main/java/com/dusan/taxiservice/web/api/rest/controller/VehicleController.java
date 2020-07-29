package com.dusan.taxiservice.web.api.rest.controller;

import com.dusan.taxiservice.core.service.VehicleService;
import com.dusan.taxiservice.core.service.model.CreateVehicleCommand;
import com.dusan.taxiservice.core.service.model.PageParams;
import com.dusan.taxiservice.core.service.model.UpdateVehicleCommand;
import com.dusan.taxiservice.core.service.model.VehicleDto;
import com.dusan.taxiservice.web.api.rest.docs.Descriptions;
import com.dusan.taxiservice.web.api.rest.model.request.UpdateVehicleRequest;
import com.dusan.taxiservice.web.api.rest.model.response.PageResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.dusan.taxiservice.web.api.rest.docs.OpenApiConfig;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

@Tag(name = "Vehicle")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@RequestMapping(Mappings.VEHICLE_BASE_PATH)
@AllArgsConstructor
public class VehicleController {

    private VehicleService vehicleService;
    
    @Operation(summary = "Create vehicle", description = Descriptions.CREATE_VEHICLE,
            responses = {@ApiResponse(responseCode = "201", description = "Successful operation"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createVehicle(@Valid @RequestBody CreateVehicleCommand createCommand) {
        vehicleService.createVehicle(createCommand);
    }
        
    @Operation(summary = "Retrieve vehicles", description = Descriptions.RETRIEVE_VEHICLES)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseWrapper<VehicleDto> retrieveAllVehicles(@Valid @ParameterObject PageParams pageParams){
        return ControllerUtils.createPageResponseWrapper(vehicleService.findAllVehicles(pageParams));
    }
    
    @Operation(summary = "Retrieve vehicle", description = Descriptions.RETRIEVE_VEHICLE,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping(value = "/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDto retrieveVehicle(@PathVariable long vehicleId) {
        return vehicleService.findVehicle(vehicleId);
    }
    
    @Operation(summary = "Update vehicle", description = Descriptions.UPDATE_VEHICLE, 
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Not found"),
                         @ApiResponse(responseCode = "400", description = "Required field is missing or invalid")})
    @PutMapping(value = "/{vehicleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateVehicle(
            @PathVariable long vehicleId,
            @Valid @RequestBody UpdateVehicleRequest updateRequest) {
        UpdateVehicleCommand updateCommand = new UpdateVehicleCommand();
        updateCommand.setVehicleId(vehicleId);
        BeanUtils.copyProperties(updateRequest, updateCommand);
        vehicleService.updateVehicle(updateCommand);
    }
}
