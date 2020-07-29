package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelRideCommand {

    private long rideId;
    private String clientUsername;
}
