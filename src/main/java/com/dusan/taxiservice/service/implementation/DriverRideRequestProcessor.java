package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dto.response.RideResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.repository.RideRepository;
import com.dusan.taxiservice.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.dao.repository.DriverRepository;
import com.dusan.taxiservice.dto.request.SuccessfulRideRequest;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Location;
import com.dusan.taxiservice.service.exception.ConflictException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class DriverRideRequestProcessor {
    
    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private ConversionService conversionService;
    

    public RideResponse acceptRide(long rideId, String driverUsername) {
        Ride ride = getRideFromDatabase(rideId);
        if (ride.getRideStatus().getId() != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride is not in CREATED status");
        Driver driver = getDriverFromDatabase(driverUsername);
        if (driver.getVehicle().getVehicleType().getId() != ride.getVehicleType().getId())
            throw new ConflictException("Driver vehicle type and requested vehicle type does not match");
        if (driver.getStatus().getId() != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver needs to be in WAITING FOR RIDE status");
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.ACCEPTED.getId()));
        Ride acceptedRide = rideRepository.save(ride);
        return conversionService.convert(acceptedRide, RideResponse.class);
    }
    
    private Ride getRideFromDatabase(long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists"));
    }
    
    private Driver getDriverFromDatabase(String driverUsername) {
        return driverRepository.findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }
    
    public RideResponse setFailedStatus(long rideId, String driverUsername) {
        Ride ride = getRideFromDatabase(rideId, driverUsername);
        long rideStatus = ride.getRideStatus().getId();
        if (!isRideActive(rideStatus))
            throw new ConflictException("Only ACTIVE drives can be canceled");      
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FAILED.getId()));
        Ride failedRide = rideRepository.save(ride);
        return conversionService.convert(failedRide, RideResponse.class);
    }

    public RideResponse setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest) {
        Ride ride = getRideFromDatabase(rideId, driverUsername);
        long driveStatus = ride.getRideStatus().getId();
        if (!isRideActive(driveStatus))
            throw new ConflictException("Ride is not active.");
        ride.setDestination(new Location(successfulRideRequest.getDestination().getLatitude(), successfulRideRequest.getDestination().getLongitude()));
        ride.setValue(successfulRideRequest.getValue());
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.SUCCESSFUL.getId()));   
        Ride successfulRide = rideRepository.save(ride);
        return conversionService.convert(successfulRide, RideResponse.class);
    }
  
    private Ride getRideFromDatabase(long rideId, String driverUsername) {
        Driver driver = driverRepository.getOne(driverUsername);
        return rideRepository.findByIdAndDriver(rideId, driver)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists."));
    }
    
    private boolean isRideActive(long driveStatus) {
        return driveStatus == RideStatuses.ACCEPTED.getId() ||
                           driveStatus == RideStatuses.FORMED.getId() || 
                           driveStatus == RideStatuses.PROCESSED.getId();
    }  
}
