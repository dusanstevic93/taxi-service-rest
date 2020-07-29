package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.service.RideReportService;
import com.dusan.taxiservice.core.service.model.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.core.dao.repository.DriverRepository;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.Location;
import com.dusan.taxiservice.core.service.exception.ConflictException;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
class DriverRideCommandExecutor {
    
    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private DriverRepository driverRepository;
    private ConversionService conversionService;
    private RideReportService reportService;
    

    @Transactional
    public RideDto acceptRide(AcceptRideCommand acceptCommand) {
        Ride ride = getRideFromDatabase(acceptCommand.getRideId());

        // validate if ride status is correct
        if (ride.getRideStatus().getId() != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride is not in CREATED status");

        Driver driver = getDriverFromDatabase(acceptCommand.getDriverUsername());

        // validate if driver vehicle type is correct
        if (driver.getVehicle().getVehicleType().getId() != ride.getVehicleType().getId())
            throw new ConflictException("Driver vehicle type and requested vehicle type does not match");

        // validate if driver status is correct
        if (driver.getStatus().getId() != DriverStatuses.WAITING_FOR_RIDE.getId())
            throw new ConflictException("Driver needs to be in WAITING FOR RIDE status");

        // update ride entity
        ride.setDriver(driver);
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.ACCEPTED.getId()));
        Ride acceptedRide = rideRepository.save(ride);

        // update driver status
        driverRepository.updateStatus(acceptCommand.getDriverUsername(), DriverStatuses.ON_RIDE.getId());

        return conversionService.convert(acceptedRide, RideDto.class);
    }
    
    private Ride getRideFromDatabase(long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride does not exists"));
    }
    
    private Driver getDriverFromDatabase(String driverUsername) {
        return driverRepository.findByUsernameFetchVehicle(driverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    @Transactional
    public RideDto setFailedStatus(SetFailedStatusCommand failedCommand) {
        Ride ride = getRideFromDatabase(failedCommand.getRideId(), failedCommand.getDriverUsername());

        // validate if ride status is correct
        long rideStatus = ride.getRideStatus().getId();
        if (!isRideActive(rideStatus))
            throw new ConflictException("Only ACTIVE drives can be canceled");

        // update ride entity
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.FAILED.getId()));
        Ride failedRide = rideRepository.save(ride);

        // update driver status
        driverRepository.updateStatus(failedCommand.getDriverUsername(), DriverStatuses.WAITING_FOR_RIDE.getId());

        // create ride report
        CreateReportCommand createReportCommand = new CreateReportCommand();
        createReportCommand.setRideId(failedCommand.getRideId());
        createReportCommand.setDriverUsername(failedCommand.getDriverUsername());
        createReportCommand.setReport(failedCommand.getReport());
        reportService.createReport(createReportCommand);

        return conversionService.convert(failedRide, RideDto.class);
    }

    @Transactional
    public RideDto setSuccessfulStatus(SetSuccessfulStatusCommand successfulCommand) {
        Ride ride = getRideFromDatabase(successfulCommand.getRideId(), successfulCommand.getDriverUsername());

        // validate if ride status is correct
        long rideStatus = ride.getRideStatus().getId();
        if (!isRideActive(rideStatus))
            throw new ConflictException("Ride is not active.");

        // update ride entity
        ride.setDestination(new Location(successfulCommand.getDestinationLatitude(), successfulCommand.getDestinationLongitude()));
        ride.setValue(successfulCommand.getValue());
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.SUCCESSFUL.getId()));   
        Ride successfulRide = rideRepository.save(ride);

        // update driver status
        driverRepository.updateStatus(successfulCommand.getDriverUsername(), DriverStatuses.WAITING_FOR_RIDE.getId());

        return conversionService.convert(successfulRide, RideDto.class);
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
