package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateVehicleCommand {

    private long vehicleId;

    @Min(1970)
    @Max(2100)
    private Integer productionYear;

    @NotBlank
    private String licencePlate;

    @NotNull
    private VehicleTypes type;
}
