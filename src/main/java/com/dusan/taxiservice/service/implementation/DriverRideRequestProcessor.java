package com.dusan.taxiservice.service.implementation;

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
    

    public void acceptRide(long rideId, String driverUsername) {
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
        rideRepository.save(ride);
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
    
    public void setFailedStatus(long rideId, String driverUsername) {
        Ride ride = findRide(rideId, driverUsername);
        long rideStatus = ride.getRideStatus().getId();
        if (!isRideActive(rideStatus))
            throw new ConflictException("Only ACTIVE drives can be canceled");      
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FAILED.getId()));
        rideRepository.save(ride);
    }

    public void setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest) {
        Ride ride = findRide(rideId, driverUsername);    
        long driveStatus = ride.getRideStatus().getId();
        if (!isRideActive(driveStatus))
            throw new ConflictException("Ride is not active.");
        ride.setDestination(new Location(successfulRideRequest.getDestination().getLatitude(), successfulRideRequest.getDestination().getLongitude()));
        ride.setValue(successfulRideRequest.getValue());
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.SUCCESSFUL.getId()));   
        rideRepository.save(ride);
    }
  
    private Ride findRide(long rideId, String driverUsername) {
        Driver driver = driverRepository.getOne(driverUsername);
        Ride ride = rideRepository.findByIdAndDriver(rideId, driver)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists."));
        return ride;
    }
    
    private boolean isRideActive(long driveStatus) {
        boolean isActive = driveStatus == RideStatuses.ACCEPTED.getId() || 
                           driveStatus == RideStatuses.FORMED.getId() || 
                           driveStatus == RideStatuses.PROCESSED.getId();
        return isActive;
    }  
}
