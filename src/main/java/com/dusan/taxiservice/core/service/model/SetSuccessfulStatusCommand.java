package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SetSuccessfulStatusCommand {

    private long rideId;
    private String driverUsername;
    private int value;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
}
