package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.service.model.DriverDto;
import com.dusan.taxiservice.core.service.model.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class DriverEntityToDriverDto implements Converter<Driver, DriverDto> {

    private VehicleEntityToVehicleDto vehicleEntityToVehicleDto;

    @Override
    public DriverDto convert(Driver entity) {
        DriverDto dto = new DriverDto();
        BeanUtils.copyProperties(entity, dto, "location", "status", "vehicle");
        if (entity.getCurrentLocation() != null)
            dto.setLocation(new LocationDto(entity.getCurrentLocation().getLatitude(), entity.getCurrentLocation().getLongitude()));
        dto.setStatus(DriverStatuses.fromId(entity.getStatus().getId()));
        dto.setVehicle(vehicleEntityToVehicleDto.convert(entity.getVehicle()));
        return dto;
    }
}
