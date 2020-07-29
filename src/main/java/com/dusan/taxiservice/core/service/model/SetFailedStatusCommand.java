package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetFailedStatusCommand {

    private long rideId;
    private String driverUsername;
    private String report;
}
