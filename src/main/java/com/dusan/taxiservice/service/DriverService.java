package com.dusan.taxiservice.service;

import java.util.List;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.request.DriverQueryParams;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.DriverResponse;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.enums.DriverStatuses;

public interface DriverService {

    void updateLocation(String username, LocationDto location);
    void updateStatus(String username, DriverStatuses status);
    DriverStatuses getCurrentStatus(String username);
    List<DriverResponse> findAllDrivers(DriverQueryParams queryParams, PageParams pageParams);
    List<UserProfileResponse> findAllVehicleDrivers(long vehicleId);  
}
