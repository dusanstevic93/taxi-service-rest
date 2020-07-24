package com.dusan.taxiservice.service.implementation;

import java.time.LocalDateTime;

import com.dusan.taxiservice.dto.response.RideResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.repository.DispatcherRepository;
import com.dusan.taxiservice.dao.repository.RideRepository;
import com.dusan.taxiservice.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.dao.repository.DriverRepository;
import com.dusan.taxiservice.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.FormRideRequest;
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
class DispatcherRideRequestProcessor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private DispatcherRepository dispatcherRepository;
    private ConversionService conversionService;
    
    
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
        return conversionService.convert(formedRide, RideResponse.class);
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
        return conversionService.convert(processedRide, RideResponse.class);
    }
    
    private Ride findRide(long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
    
    private Driver findDriver(String driverUsername) {
        return driverRepository
                .findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver is not found"));
    }
    
    private void checkDriverStatus(long statusId) {
        if (statusId != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver is not available");
    }
}
