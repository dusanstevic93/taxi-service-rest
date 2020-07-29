package com.dusan.taxiservice.core.service.implementation;

import java.time.LocalDateTime;

import com.dusan.taxiservice.core.service.model.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.ClientRepository;
import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.core.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.core.entity.Client;
import com.dusan.taxiservice.core.entity.Location;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.RideStatus;
import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.service.exception.ConflictException;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@AllArgsConstructor
@Validated
class ClientRideCommandExecutor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private ClientRepository clientRepository;
    private ConversionService conversionService;
    

    public RideDto createRide(@Valid CreateRideCommand command) {
        checkIfClientAlreadyCreatedRide(command.getClientUsername());

        // create ride entity
        Ride ride = new Ride();         
        ride.setCreationDateTime(LocalDateTime.now());
        ride.setStartingLocation(new Location(command.getLatitude(), command.getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(command.getVehicleType().getId()));
        ride.setClient(clientRepository.getOne(command.getClientUsername()));
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CREATED.getId()));
        Ride createdRide = rideRepository.save(ride);

        return conversionService.convert(createdRide, RideDto.class);
    }
    
    private void checkIfClientAlreadyCreatedRide(String clientUsername) {
        Client client = clientRepository.getOne(clientUsername);
        RideStatus status = rideStatusRepository.getOne(RideStatuses.CREATED.getId());
        boolean createdRideExists = rideRepository.existsByClientAndRideStatus(client, status);
        if (createdRideExists)
            throw new ConflictException("Client has already created ride");
    }

    public RideDto updateRide(@Valid UpdateRideCommand updateCommand) {
        Ride ride = getRideFromDatabase(updateCommand.getRideId(), updateCommand.getClientUsername());

        checkIfRideIsInCreatedStatus(ride.getRideStatus().getId());

        // update ride entity
        ride.setStartingLocation(new Location(updateCommand.getLatitude(), updateCommand.getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(updateCommand.getVehicleType().getId()));
        Ride updatedRide = rideRepository.save(ride);

        return conversionService.convert(updatedRide, RideDto.class);
    }

    public RideDto cancelRide(@Valid CancelRideCommand cancelCommand) {
        Ride ride = getRideFromDatabase(cancelCommand.getRideId(), cancelCommand.getClientUsername());

        checkIfRideIsInCreatedStatus(ride.getRideStatus().getId());

        // update ride entity
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CANCELED.getId()));     
        Ride canceledRide = rideRepository.save(ride);

        return conversionService.convert(canceledRide, RideDto.class);
    }
    
    private void checkIfRideIsInCreatedStatus(long statusId) {
        if (statusId != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride is not in created status");
    }
    
    public void rateRide(@Valid RateRideCommand rateCommand) {
        Ride ride = getRideFromDatabase(rateCommand.getRideId(), rateCommand.getClientUsername());
        checkIfRideIsAlreadyRated(ride);
        ride.setRating(rateCommand.getRating());
        rideRepository.save(ride);
    }

    private void checkIfRideIsAlreadyRated(Ride ride){
        if (ride.getRating() != 0)
            throw new ConflictException("Ride is already rated");
    }
    
    private Ride getRideFromDatabase(long rideId, String clientUsername) {
        Client client = clientRepository.getOne(clientUsername);
        return rideRepository.findByIdAndClient(rideId, client)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
}
