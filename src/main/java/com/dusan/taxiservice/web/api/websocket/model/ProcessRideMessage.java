package com.dusan.taxiservice.web.api.websocket.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProcessRideMessage {

    private long rideId;

    @NotBlank
    private String driverUsername;
}
