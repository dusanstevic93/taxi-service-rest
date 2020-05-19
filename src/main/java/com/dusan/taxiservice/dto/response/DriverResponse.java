package com.dusan.taxiservice.dto.response;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.entity.enums.DriverStatuses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverResponse extends UserProfileResponse {

    private LocationDto location;
    private VehicleResponse vehicle;
    private DriverStatuses status;
}
