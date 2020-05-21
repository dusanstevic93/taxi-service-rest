package com.dusan.taxiservice.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVehicleRequest {

    @NotNull
    private Integer productionYear;
    
    @NotBlank
    private String licencePlate;
    
    @NotNull
    private VehicleTypes type;  
}
