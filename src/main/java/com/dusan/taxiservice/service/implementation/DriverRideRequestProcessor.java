package com.dusan.taxiservice.service.implementation;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dao.RideStatusRepository;
import com.dusan.taxiservice.dao.DriverRepository;
import com.dusan.taxiservice.dto.request.SuccessfulRideRequest;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Location;
import com.dusan.taxiservice.exception.ConflictException;
import com.dusan.taxiservice.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class DriverRideRequestProcessor {
    
    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private ConversionService conversion;
    

    public RideResponse acceptRide(long rideId, String driverUsername) {
        Ride ride = findRide(rideId);
        if (ride.getRideStatus().getId() != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride not in created status"); 
        Driver driver = findDriver(driverUsername);
        if (driver.getVehicle().getVehicleType().getId() != ride.getVehicleType().getId())
            throw new ConflictException("Driver's vehicle type and requested vehicle type does not match");
        if (driver.getStatus().getId() != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver needs to be in WAITING FOR RIDE status");
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.ACCEPTED.getId()));
        Ride acceptedRide = rideRepository.save(ride); 
        return conversion.convert(acceptedRide, RideResponse.class);
    }
    
    private Ride findRide(long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists"));
        return ride;
    }
    
    private Driver findDriver(String driverUsername) {
        Driver driver = driverRepository
                .findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        return driver;
    }
    
    public RideResponse setFailedStatus(long rideId, String driverUsername) {
        Ride ride = findRide(rideId, driverUsername);
        long rideStatus = ride.getRideStatus().getId();
        if (!isDriveActive(rideStatus))
            throw new ConflictException("Only ACTIVE drives can be canceled");      
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FAILED.getId()));
        Ride failedRide = rideRepository.save(ride);
        return conversion.convert(failedRide, RideResponse.class);
    }

    public RideResponse setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest) {
        Ride ride = findRide(rideId, driverUsername);    
        long driveStatus = ride.getRideStatus().getId();
        if (!isDriveActive(driveStatus))
            throw new ConflictException("Ride is not active.");
        ride.setDestination(new Location(successfulRideRequest.getDestination().getLatitude(), successfulRideRequest.getDestination().getLongitude()));
        ride.setValue(successfulRideRequest.getValue());
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.SUCCESSFUL.getId()));   
        Ride successRide = rideRepository.save(ride);   
        return conversion.convert(successRide, RideResponse.class);
    }
  
    private Ride findRide(long rideId, String driverUsername) {
        Driver driver = driverRepository.getOne(driverUsername);
        Ride ride = rideRepository.findByIdAndDriver(rideId, driver)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists."));
        return ride;
    }
    
    private boolean isDriveActive(long driveStatus) {
        boolean isActive = driveStatus == RideStatuses.ACCEPTED.getId() || 
                           driveStatus == RideStatuses.FORMED.getId() || 
                           driveStatus == RideStatuses.PROCESSED.getId();
        return isActive;
    }  
}
