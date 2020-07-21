package com.dusan.taxiservice.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.response.DriverResponse;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.enums.DriverStatuses;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DriverToDriverResponse implements Converter<Driver, DriverResponse> {

    private VehicleToVehicleResponse vehicleToVehicleResponse;
    
    @Override
    public DriverResponse convert(Driver driver) {
        DriverResponse response = new DriverResponse();
        BeanUtils.copyProperties(driver, response, "location", "status", "vehicle");
        if (driver.getCurrentLocation() != null)
            response.setLocation(new LocationDto(driver.getCurrentLocation().getLatitude(), driver.getCurrentLocation().getLongitude()));
        response.setStatus(DriverStatuses.fromId(driver.getStatus().getId()));
        response.setVehicle(vehicleToVehicleResponse.convert(driver.getVehicle()));
        return response;
    }

    
}
