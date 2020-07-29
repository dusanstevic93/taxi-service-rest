package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptRideCommand {

    private long rideId;
    private String driverUsername;
}
