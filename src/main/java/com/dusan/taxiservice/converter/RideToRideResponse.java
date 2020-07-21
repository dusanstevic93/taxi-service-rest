package com.dusan.taxiservice.converter;

import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.enums.VehicleTypes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class RideToRideResponse implements Converter<Ride, RideResponse> {
    
    @Override
    public RideResponse convert(Ride ride) {
        RideResponse response = new RideResponse();
        response.setId(ride.getId());
        response.setCreationDateTime(ride.getCreationDateTime());
        response.setValue(ride.getValue());
        response.setStartingLocation(new LocationDto(ride.getStartingLocation().getLatitude(), ride.getStartingLocation().getLongitude()));
        response.setVehicleType(VehicleTypes.fromId(ride.getVehicleType().getId()));
        if (ride.getDestination() != null)
            response.setDestination(new LocationDto(ride.getDestination().getLatitude(), ride.getDestination().getLongitude()));
        if (ride.getDriver() != null)
            response.setDriver(ride.getDriver().getUsername());
        if (ride.getDispatcher() != null)
            response.setDispatcher(ride.getDispatcher().getUsername());
        if (ride.getClient() != null)
            response.setClient(ride.getClient().getUsername());
        response.setRideStatus(RideStatuses.fromId(ride.getRideStatus().getId()));
        response.setRating(ride.getRating());
        return response;
    }

}
