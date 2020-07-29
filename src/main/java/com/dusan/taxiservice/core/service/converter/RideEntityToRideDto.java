package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.service.model.LocationDto;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import com.dusan.taxiservice.core.service.model.RideDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class RideEntityToRideDto implements Converter<Ride, RideDto> {

    @Override
    public RideDto convert(Ride entity) {
        RideDto dto = new RideDto();
        dto.setId(entity.getId());
        dto.setCreationDateTime(entity.getCreationDateTime());
        dto.setValue(entity.getValue());
        dto.setStartingLocation(new LocationDto(entity.getStartingLocation().getLatitude(), entity.getStartingLocation().getLongitude()));
        dto.setVehicleType(VehicleTypes.fromId(entity.getVehicleType().getId()));
        if (entity.getDestination() != null)
            dto.setDestination(new LocationDto(entity.getDestination().getLatitude(), entity.getDestination().getLongitude()));
        if (entity.getDriver() != null)
            dto.setDriver(entity.getDriver().getUsername());
        if (entity.getDispatcher() != null)
            dto.setDispatcher(entity.getDispatcher().getUsername());
        if (entity.getClient() != null)
            dto.setClient(entity.getClient().getUsername());
        dto.setRideStatus(RideStatuses.fromId(entity.getRideStatus().getId()));
        dto.setRating(entity.getRating());
        return dto;
    }
}
