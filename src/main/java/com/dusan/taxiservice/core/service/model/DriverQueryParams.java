package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverQueryParams {

    private VehicleTypes vehicleType;
    private DriverStatuses driverStatus;  
}
