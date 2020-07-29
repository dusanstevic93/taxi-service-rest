package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RateRideCommand {

    private long rideId;

    @NotBlank
    private String clientUsername;

    @Min(1)
    @Max(5)
    private int rating;
}
