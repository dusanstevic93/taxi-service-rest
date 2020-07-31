package com.dusan.taxiservice.web.api.websocket.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class SuccessfulRideMessage {

    private long rideId;

    @Min(1)
    private int value;

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    @Digits(integer = 2, fraction = 8)
    private BigDecimal destinationLatitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    @Digits(integer = 3, fraction = 8)
    private BigDecimal destinationLongitude;
}
