package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FormRideCommand {

    private String dispatcherUsername;
    private String driverUsername;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private VehicleTypes vehicleType = VehicleTypes.CAR;
}
