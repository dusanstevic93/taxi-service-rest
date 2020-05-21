package com.dusan.taxiservice.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRideRequest {

    @NotNull
    @Valid
    private LocationDto startingLocation;
    
    private VehicleTypes vehicleType = VehicleTypes.CAR;   
}
