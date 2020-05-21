package com.dusan.taxiservice.dto.request;

import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverQueryParams {

    private VehicleTypes vehicleType;
    private DriverStatuses driverStatus;  
}
