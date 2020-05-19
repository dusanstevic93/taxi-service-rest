package com.dusan.taxiservice.service.implementation;

import java.time.LocalDateTime;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.DispatcherRepository;
import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dao.RideStatusRepository;
import com.dusan.taxiservice.dao.DriverRepository;
import com.dusan.taxiservice.dao.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.FormRideRequest;
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
class DispatcherRideRequestProcessor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private DispatcherRepository dispatcherRepository;
    private ConversionService conversion;
    
    
    public RideResponse formRide(String dispatcherUsername, String driverUsername, FormRideRequest formRideRequest) {
        Driver driver = findDriver(driverUsername);
        checkDriverStatus(driver.getStatus().getId());
        long driverVehicleTypeId = driver.getVehicle().getId();
        long requestedVehicleTypeId = formRideRequest.getVehicleType().getId();
        if (driverVehicleTypeId != requestedVehicleTypeId)
            throw new ConflictException("Driver's vehicle type and requested vehicle type does not match");
        Ride ride = new Ride();         
        ride.setCreationDateTime(LocalDateTime.now());
        ride.setVehicleType(vehicleTypeRepository.getOne(formRideRequest.getVehicleType().getId()));
        ride.setStartingLocation(new Location(formRideRequest.getStartingLocation().getLatitude(), formRideRequest.getStartingLocation().getLongitude()));
        ride.setDispatcher(dispatcherRepository.getOne(dispatcherUsername));
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FORMED.getId()));      
        Ride formedRide = rideRepository.save(ride); 
        return conversion.convert(formedRide, RideResponse.class);
    }
    
    public RideResponse processRide(long rideId, String dispatcherUsername, String driverUsername) {
        Ride ride = findRide(rideId);     
        if (ride.getRideStatus().getId() != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride not in created status");
        Driver driver = findDriver(driverUsername);
        checkDriverStatus(driver.getStatus().getId());
        long driverVehicleTypeId = driver.getVehicle().getId();
        long requestedVehicleTypeId = ride.getVehicleType().getId();
        if (driverVehicleTypeId != requestedVehicleTypeId)
            throw new ConflictException("Driver's vehicle type and requested vehicle type does not match");
        ride.setDispatcher(dispatcherRepository.getOne(dispatcherUsername));
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.PROCESSED.getId()));
        Ride processedRide = rideRepository.save(ride);
        return conversion.convert(processedRide, RideResponse.class);
    }
    
    private Ride findRide(long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return ride;
    }
    
    private Driver findDriver(String driverUsername) {
        Driver driver = driverRepository
                .findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver is not found"));
        return driver;
    }
    
    private void checkDriverStatus(long statusId) {
        if (statusId != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver is not available");
    }
}
