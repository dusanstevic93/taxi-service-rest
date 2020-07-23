package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dao.repository.ClientRepository;
import com.dusan.taxiservice.dao.repository.RideRepository;
import com.dusan.taxiservice.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideStatus;
import com.dusan.taxiservice.entity.VehicleType;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import com.dusan.taxiservice.service.exception.ConflictException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRideRequestProcessorTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideStatusRepository rideStatusRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ClientRideRequestProcessor clientRequestProcessor;

    @Test
    void testCreateRideShouldBeSuccessful(){
        // given
        CreateRideRequest createRequest = getCreateRideRequestModel();

        Client client = getClientModel();
        given(clientRepository.getOne(client.getUsername())).willReturn(client);

        VehicleType vehicleType = getVehicleTypeModel(createRequest.getVehicleType());
        given(vehicleTypeRepository.getOne(createRequest.getVehicleType().getId())).willReturn(vehicleType);

        RideStatus rideStatus = getRideStatusModel(RideStatuses.CREATED);
        given(rideStatusRepository.getOne(rideStatus.getId())).willReturn(rideStatus);

        // when
        clientRequestProcessor.createRide(client.getUsername(), createRequest);

        // then
        ArgumentCaptor<Ride> argumentCaptor = ArgumentCaptor.forClass(Ride.class);
        then(rideRepository).should().save(argumentCaptor.capture());

        Ride createdRide = argumentCaptor.getValue();

        assertAll(
                () -> assertNotNull(createdRide.getCreationDateTime()),
                () -> assertEquals(createRequest.getStartingLocation().getLatitude(), createdRide.getStartingLocation().getLatitude()),
                () -> assertEquals(createRequest.getStartingLocation().getLongitude(), createdRide.getStartingLocation().getLongitude()),
                () -> assertEquals(createRequest.getVehicleType().getId(), createdRide.getVehicleType().getId()),
                () -> assertEquals(client.getUsername(), createdRide.getClient().getUsername()),
                () -> assertEquals(RideStatuses.CREATED.getId(), createdRide.getRideStatus().getId())
        );
    }

    @Test
    void testCreateRideAlreadyCreated(){
        // given
        CreateRideRequest createRequest = getCreateRideRequestModel();

        Client client = getClientModel();
        given(clientRepository.getOne(client.getUsername())).willReturn(client);

        RideStatus rideStatus = getRideStatusModel(RideStatuses.CREATED);
        given(rideStatusRepository.getOne(rideStatus.getId())).willReturn(rideStatus);

        given(rideRepository.existsByClientAndRideStatus(client, rideStatus)).willReturn(true);

        // when
        Executable createRide = () -> clientRequestProcessor.createRide(client.getUsername(), createRequest);

        // then
        assertThrows(ConflictException.class, createRide);
    }

    @Test
    void testUpdateRide(){
        // given
        CreateRideRequest updateRequest = getCreateRideRequestModel();
        Ride rideToUpdate = getRideModel(RideStatuses.CREATED, VehicleTypes.VAN);

        Client client = getClientModel();
        given(clientRepository.getOne(client.getUsername())).willReturn(client);

        given(rideRepository.findByIdAndClient(rideToUpdate.getId(), client)).willReturn(Optional.of(rideToUpdate));

        VehicleType vehicleType = getVehicleTypeModel(updateRequest.getVehicleType());
        given(vehicleTypeRepository.getOne(vehicleType.getId())).willReturn(vehicleType);

        // when
        clientRequestProcessor.updateRide(rideToUpdate.getId(), client.getUsername(), updateRequest);

        // then
        ArgumentCaptor<Ride> argumentCaptor = ArgumentCaptor.forClass(Ride.class);
        then(rideRepository).should().save(argumentCaptor.capture());
        Ride updatedRide = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(updateRequest.getStartingLocation().getLongitude(), updatedRide.getStartingLocation().getLongitude()),
                () -> assertEquals(updateRequest.getStartingLocation().getLatitude(), updatedRide.getStartingLocation().getLatitude()),
                () -> assertEquals(updateRequest.getVehicleType().getId(), updatedRide.getVehicleType().getId())
        );
    }

    @Test
    void testUpdateRideNotFound(){
        // given
        long rideId = 1L;
        CreateRideRequest updateRequest = getCreateRideRequestModel();
        Client client = getClientModel();
        given(clientRepository.getOne(client.getUsername())).willReturn(client);
        given(rideRepository.findByIdAndClient(rideId, client)).willReturn(Optional.empty());

        // when
        Executable updateRide = () -> clientRequestProcessor.updateRide(rideId, client.getUsername(), updateRequest);

        // then
        assertThrows(ResourceNotFoundException.class, updateRide);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatuses.class, names = "CREATED", mode = EnumSource.Mode.EXCLUDE)
    void testUpdateRideNotInCreatedStatus(RideStatuses status){
        // given
        Ride rideToUpdate = getRideModel(status);
        CreateRideRequest updateRequest = getCreateRideRequestModel();
        given(rideRepository.findByIdAndClient(eq(rideToUpdate.getId()), any())).willReturn(Optional.of(rideToUpdate));

        // when
        Executable update = () -> clientRequestProcessor.updateRide(rideToUpdate.getId(), "test", updateRequest);

        // then
        assertThrows(ConflictException.class, update);
    }

    @Test
    void testCancelRideShouldBeSuccessful(){
        // given
        Client client = getClientModel();
        Ride rideToCancel = getRideModel(RideStatuses.CREATED);
        given(rideRepository.findByIdAndClient(eq(rideToCancel.getId()), any())).willReturn(Optional.of(rideToCancel));

        RideStatus canceledStatus = getRideStatusModel(RideStatuses.CANCELED);
        given(rideStatusRepository.getOne(canceledStatus.getId())).willReturn(canceledStatus);

        // when
        clientRequestProcessor.cancelRide(rideToCancel.getId(), client.getUsername());

        // then
        ArgumentCaptor<Ride> argumentCaptor = ArgumentCaptor.forClass(Ride.class);
        then(rideRepository).should().save(argumentCaptor.capture());
        Ride cancelledRide = argumentCaptor.getValue();

        assertEquals(RideStatuses.CANCELED.getId(), cancelledRide.getRideStatus().getId());
    }

    @Test
    void testCancelRideNotFound(){
        // given
        long rideId = 1L;
        String client = "test";
        given(rideRepository.findByIdAndClient(eq(rideId), any())).willReturn(Optional.empty());

        // when
        Executable cancelRide = () -> clientRequestProcessor.cancelRide(rideId, client);

        // then
        assertThrows(ResourceNotFoundException.class, cancelRide);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatuses.class, names = {"CREATED"}, mode = EnumSource.Mode.EXCLUDE)
    void testCancelRideNotInCreatedStatus(RideStatuses status){
        // given
        Ride rideToCancel = getRideModel(status);
        given(rideRepository.findByIdAndClient(eq(rideToCancel.getId()), any())).willReturn(Optional.of(rideToCancel));

        // when
        Executable cancelRide = () -> clientRequestProcessor.cancelRide(rideToCancel.getId(), "client");

        // then
        assertThrows(ConflictException.class, cancelRide);
    }

    @Test
    void testRateRideShouldBeSuccessful(){
        // given
        int rating = 5;
        Ride rideToRate = getRideModel(RideStatuses.SUCCESSFUL);
        given(rideRepository.findByIdAndClient(eq(rideToRate.getId()), any())).willReturn(Optional.of(rideToRate));

        // when
        clientRequestProcessor.rateRide(rideToRate.getId(), "client", rating);

        // then
        ArgumentCaptor<Ride> argumentCaptor = ArgumentCaptor.forClass(Ride.class);
        then(rideRepository).should().save(argumentCaptor.capture());
        Ride ratedRide = argumentCaptor.getValue();

        assertEquals(rating, ratedRide.getRating());
    }

    @Test
    void testRateRideAlreadyRated(){
        // given
        int rating = 5;
        Ride rideToRate = getRideModel(RideStatuses.SUCCESSFUL);
        rideToRate.setRating(4);
        given(rideRepository.findByIdAndClient(eq(rideToRate.getId()), any())).willReturn(Optional.of(rideToRate));

        // when
        Executable rateRide = () -> clientRequestProcessor.rateRide(rideToRate.getId(), "client", rating);

        // then
        assertThrows(ConflictException.class, rateRide);
    }

    ///////////////////// models //////////////////////

    private CreateRideRequest getCreateRideRequestModel(){
        CreateRideRequest request = new CreateRideRequest();
        request.setStartingLocation(new LocationDto(-10.123456, 10.123456));
        request.setVehicleType(VehicleTypes.CAR);
        return request;
    }

    private Client getClientModel(){
        Client client = new Client();
        client.setUsername("test");
        return client;
    }

    private Ride getRideModel(RideStatuses status){
        return getRideModel(status, VehicleTypes.CAR);
    }

    private Ride getRideModel(RideStatuses status, VehicleTypes type) {
        Ride ride = new Ride();
        ReflectionTestUtils.setField(ride, "id", 1L);
        RideStatus rideStatus = getRideStatusModel(status);
        ride.setRideStatus(rideStatus);
        VehicleType vehicleType = getVehicleTypeModel(type);
        ride.setVehicleType(vehicleType);
        return ride;
    }

    private RideStatus getRideStatusModel(RideStatuses status){
        RideStatus rideStatus = new RideStatus();
        ReflectionTestUtils.setField(rideStatus, "id", status.getId());
        return rideStatus;
    }

    private VehicleType getVehicleTypeModel(VehicleTypes type){
        VehicleType vehicleType = new VehicleType();
        ReflectionTestUtils.setField(vehicleType, "id", type.getId());
        return vehicleType;
    }
}