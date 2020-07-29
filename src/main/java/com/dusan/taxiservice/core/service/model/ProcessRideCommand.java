package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRideCommand {

    private long rideId;
    private String dispatcherUsername;
    private String driverUsername;
}
