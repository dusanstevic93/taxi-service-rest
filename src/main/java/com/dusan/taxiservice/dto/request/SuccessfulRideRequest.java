package com.dusan.taxiservice.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dusan.taxiservice.dto.LocationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessfulRideRequest {

    @NotNull
    private Integer value;
    
    @NotNull
    @Valid
    private LocationDto destination;
}
