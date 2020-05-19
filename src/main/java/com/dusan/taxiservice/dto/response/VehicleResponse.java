package com.dusan.taxiservice.dto.response;

import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponse {

    private long id;
    private int productionYear;
    private String licencePlate;
    private VehicleTypes vehicleType;
}
