package com.dusan.taxiservice.service.implementation;

import java.time.LocalDateTime;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.ClientRepository;
import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dao.RideStatusRepository;
import com.dusan.taxiservice.dao.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Location;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideStatus;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.exception.ConflictException;
import com.dusan.taxiservice.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class ClientRideRequestProcessor {

    private RideRepository rideRepository;
    private RideStatusRepository rideStatusRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private ClientRepository clientRepository;
    private ConversionService conversion;
    

    public RideResponse createRide(String clientUsername, CreateRideRequest createRideRequest) {
        checkIfClientAlreadyCreatedRide(clientUsername);
        Ride ride = new Ride();         
        ride.setCreationDateTime(LocalDateTime.now());
        ride.setStartingLocation(new Location(createRideRequest.getStartingLocation().getLatitude(), createRideRequest.getStartingLocation().getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(createRideRequest.getVehicleType().getId()));
        ride.setClient(clientRepository.getOne(clientUsername));
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CREATED.getId()));
        Ride createdRide = rideRepository.save(ride);
        return conversion.convert(createdRide, RideResponse.class);
    }
    
    private void checkIfClientAlreadyCreatedRide(String clientUsername) {
        Client client = clientRepository.getOne(clientUsername);
        RideStatus status = rideStatusRepository.getOne(RideStatuses.CREATED.getId());
        boolean createdRideExists = rideRepository.existsByClientAndRideStatus(client, status);
        if (createdRideExists)
            throw new ConflictException("Client has already created ride");
    }

    public RideResponse updateRide(long rideId, String clientUsername, CreateRideRequest updateRideRequest) {
        Ride ride = findRide(rideId, clientUsername);      
        checkStatus(ride.getRideStatus().getId());
        ride.setStartingLocation(new Location(updateRideRequest.getStartingLocation().getLatitude(), updateRideRequest.getStartingLocation().getLongitude()));
        ride.setVehicleType(vehicleTypeRepository.getOne(updateRideRequest.getVehicleType().getId()));       
        Ride updatedRide = rideRepository.save(ride);
        return conversion.convert(updatedRide, RideResponse.class);
    }

    public RideResponse cancelRide(long rideId, String clientUsername) {
        Ride ride = findRide(rideId, clientUsername);     
        checkStatus(ride.getRideStatus().getId());    
        ride.setRideStatus(rideStatusRepository.getOne(RideStatuses.CANCELED.getId()));     
        Ride canceledRide = rideRepository.save(ride); 
        return conversion.convert(canceledRide, RideResponse.class);
    }
    
    private void checkStatus(long statusId) {
        if (statusId != RideStatuses.CREATED.getId())
            throw new ConflictException("Ride not in created status");
    }
    
    public RideResponse rateRide(long rideId, String clientUsername, int rating) {
        Ride ride = findRide(rideId, clientUsername);
        checkIfRideIsAlreadyRated(ride);
        ride.setRating(rating);
        Ride ratedRide = rideRepository.save(ride);
        return conversion.convert(ratedRide, RideResponse.class);
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
