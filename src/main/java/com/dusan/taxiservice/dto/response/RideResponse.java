package com.dusan.taxiservice.dto.response;

import java.time.LocalDateTime;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideResponse {

    private long id;
    private LocalDateTime creationDateTime;
    private Integer value;
    private LocationDto startingLocation;
    private LocationDto destination;
    private VehicleTypes vehicleType;
    private String driver;
    private String dispatcher;
    private String client;
    private RideStatuses rideStatus;
    private int rating;   
}
