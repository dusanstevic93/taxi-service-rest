package com.dusan.taxiservice.core.service.implementation;

import java.time.LocalDateTime;

import com.dusan.taxiservice.core.entity.*;
import com.dusan.taxiservice.core.service.model.FormRideCommand;
import com.dusan.taxiservice.core.service.model.ProcessRideCommand;
import com.dusan.taxiservice.core.service.model.RideDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.DispatcherRepository;
import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.core.dao.repository.DriverRepository;
import com.dusan.taxiservice.core.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.service.exception.ConflictException;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;


import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
class DispatcherRideCommandExecutor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private DispatcherRepository dispatcherRepository;
    private ConversionService conversionService;
    

    @Transactional
    public RideDto formRide(FormRideCommand formCommand) {
        Driver driver = getDriverFromDatabase(formCommand.getDriverUsername());
        checkIfDriverStatusIsCorrect(driver.getStatus());

        // validate if driver vehicle type is correct
        long driverVehicleTypeId = driver.getVehicle().getId();
        long requestedVehicleTypeId = formCommand.getVehicleType().getId();
        if (driverVehicleTypeId != requestedVehicleTypeId)
            throw new ConflictException("Driver's vehicle type and requested vehicle type does not match");

        // create ride entity
        Ride ride = new Ride();         
        ride.setCreationDateTime(LocalDateTime.now());
        ride.setVehicleType(vehicleTypeRepository.getOne(formCommand.getVehicleType().getId()));
        ride.setStartingLocation(new Location(formCommand.getLatitude(), formCommand.getLongitude()));
        ride.setDispatcher(dispatcherRepository.getOne(formCommand.getDispatcherUsername()));
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FORMED.getId()));      
        Ride formedRide = rideRepository.save(ride);

        // update driver status
        driverRepository.updateStatus(formCommand.getDriverUsername(), DriverStatuses.ON_RIDE.getId());

        return conversionService.convert(formedRide, RideDto.class);
    }

    @Transactional
    public RideDto processRide(ProcessRideCommand processCommand) {
        Ride ride = getRideFromDatabase(processCommand.getRideId());
        checkIfRideStatusIsCorrect(ride.getRideStatus());

        Driver driver = getDriverFromDatabase(processCommand.getDriverUsername());
        checkIfDriverStatusIsCorrect(driver.getStatus());

        // validate if driver vehicle type is correct
        long driverVehicleTypeId = driver.getVehicle().getId();
        long requestedVehicleTypeId = ride.getVehicleType().getId();
        if (driverVehicleTypeId != requestedVehicleTypeId)
            throw new ConflictException("Driver's vehicle type and requested vehicle type does not match");

        // update ride entity
        ride.setDispatcher(dispatcherRepository.getOne(processCommand.getDispatcherUsername()));
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.PROCESSED.getId()));
        Ride processedRide = rideRepository.save(ride);

        // update driver status
        driverRepository.updateStatus(processCommand.getDriverUsername(), DriverStatuses.ON_RIDE.getId());

        return conversionService.convert(processedRide, RideDto.class);
    }
    
    private Ride getRideFromDatabase(long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
    
    private Driver getDriverFromDatabase(String driverUsername) {
        return driverRepository
                .findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver is not found"));
    }

    private void checkIfRideStatusIsCorrect(RideStatus status) {
        if (status.getId() != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride is not in created status");
    }
    
    private void checkIfDriverStatusIsCorrect(DriverStatus status) {
        if (status.getId() != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver is not available");
    }
}
