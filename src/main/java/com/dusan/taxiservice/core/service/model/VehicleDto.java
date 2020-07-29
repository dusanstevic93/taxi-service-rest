package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDto {

    private long id;
    private int productionYear;
    private String licencePlate;
    private VehicleTypes vehicleType;
}
