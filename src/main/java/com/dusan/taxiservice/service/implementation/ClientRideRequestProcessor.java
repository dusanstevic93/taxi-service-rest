package com.dusan.taxiservice.service.implementation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.repository.ClientRepository;
import com.dusan.taxiservice.dao.repository.RideRepository;
import com.dusan.taxiservice.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Location;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideStatus;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.service.exception.ConflictException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class ClientRideRequestProcessor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private ClientRepository clientRepository;
    

    public void createRide(String clientUsername, CreateRideRequest createRideRequest) {
        checkIfClientAlreadyCreatedRide(clientUsername);
        Ride ride = new Ride();         
        ride.setCreationDateTime(LocalDateTime.now());
        ride.setStartingLocation(new Location(createRideRequest.getStartingLocation().getLatitude(), createRideRequest.getStartingLocation().getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(createRideRequest.getVehicleType().getId()));
        ride.setClient(clientRepository.getOne(clientUsername));
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CREATED.getId()));
        rideRepository.save(ride);
    }
    
    private void checkIfClientAlreadyCreatedRide(String clientUsername) {
        Client client = clientRepository.getOne(clientUsername);
        RideStatus status = rideStatusRepository.getOne(RideStatuses.CREATED.getId());
        boolean createdRideExists = rideRepository.existsByClientAndRideStatus(client, status);
        if (createdRideExists)
            throw new ConflictException("Client has already created ride");
    }

    public void updateRide(long rideId, String clientUsername, CreateRideRequest updateRideRequest) {
        Ride ride = findRide(rideId, clientUsername);      
        checkStatus(ride.getRideStatus().getId());
        ride.setStartingLocation(new Location(updateRideRequest.getStartingLocation().getLatitude(), updateRideRequest.getStartingLocation().getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(updateRideRequest.getVehicleType().getId()));       
        rideRepository.save(ride);
    }

    public void cancelRide(long rideId, String clientUsername) {
        Ride ride = findRide(rideId, clientUsername);     
        checkStatus(ride.getRideStatus().getId());    
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CANCELED.getId()));     
        rideRepository.save(ride);
    }
    
    private void checkStatus(long statusId) {
        if (statusId != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride not in created status");
    }
    
    public void rateRide(long rideId, String clientUsername, int rating) {
        Ride ride = findRide(rideId, clientUsername);
        checkIfRideIsAlreadyRated(ride);
        ride.setRating(rating);
        rideRepository.save(ride);
    }

    private void checkIfRideIsAlreadyRated(Ride ride){
        if (ride.getRating() != 0)
            throw new ConflictException("Ride is already rated");
    }
    
    private Ride findRide(long rideId, String clientUsername) {
        Client client = clientRepository.getOne(clientUsername);
        Ride ride = rideRepository.findByIdAndClient(rideId, client)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return ride;
    }
}
