package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverDto {

    private LocationDto location;
    private VehicleDto vehicle;
    private DriverStatuses status;
}
