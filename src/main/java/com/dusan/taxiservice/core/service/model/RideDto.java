package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RideDto {

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
