package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.DriverDto;
import com.dusan.taxiservice.core.service.model.LocationDto;
import com.dusan.taxiservice.core.service.model.DriverQueryParams;
import com.dusan.taxiservice.core.service.model.PageParams;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DriverService {

    void updateLocation(String username, LocationDto location);
    void updateStatus(String username, DriverStatuses status);
    DriverStatuses getCurrentStatus(String username);
    Page<DriverDto> findAllDrivers(DriverQueryParams queryParams, PageParams pageParams);
    List<UserProfileDto> findAllVehicleDrivers(long vehicleId);
}
