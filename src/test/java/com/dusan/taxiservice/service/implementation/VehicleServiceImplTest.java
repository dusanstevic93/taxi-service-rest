package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dao.VehicleRepository;
import com.dusan.taxiservice.dao.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.Vehicle;
import com.dusan.taxiservice.entity.VehicleType;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private ConversionService conversion;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void testCreateVehicleShouldBeSuccessful(){
        // given
        CreateVehicleRequest testRequest = getCreateVehicleRequestModel();
        VehicleType vehicleType = new VehicleType();
        ReflectionTestUtils.setField(vehicleType, "id", testRequest.getType().getId());
        given(vehicleTypeRepository.getOne(testRequest.getType().getId())).willReturn(vehicleType);
        given(vehicleRepository.save(any())).willAnswer(invoker -> invoker.getArgument(0));
        given(conversion.convert(any(), eq(VehicleResponse.class))).willReturn(new VehicleResponse());

        // when
        vehicleService.createVehicle(testRequest);

        // then
        ArgumentCaptor<Vehicle> argumentCaptor = ArgumentCaptor.forClass(Vehicle.class);
        then(conversion).should().convert(argumentCaptor.capture(), eq(VehicleResponse.class));

        Vehicle createdVehicle = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(testRequest.getLicencePlate(), createdVehicle.getLicencePlate()),
                () -> assertEquals(testRequest.getProductionYear(), createdVehicle.getProductionYear()),
                () -> assertEquals(testRequest.getType().getId(), createdVehicle.getVehicleType().getId())
        );

    }

    @Test
    void testFindVehicleShouldBeSuccessful(){
        // given
        long vehicleId = 1L;
        Vehicle vehicle = getVehicleModel();
        given(vehicleRepository.findById(vehicleId)).willReturn(Optional.of(vehicle));

        // when
        vehicleService.findVehicle(vehicleId);

        // then
        then(conversion).should().convert(vehicle, VehicleResponse.class);
    }

    @Test
    void testFindVehicleNotFound(){
        // given
        long vehicleId = 1L;
        given(vehicleRepository.findById(vehicleId)).willReturn(Optional.empty());

        // when
        Executable find = () -> vehicleService.findVehicle(vehicleId);

        // then
        assertThrows(ResourceNotFoundException.class, find);
    }

    @Test
    void testFindAllVehicles(){

    }

    private CreateVehicleRequest getCreateVehicleRequestModel(){
        CreateVehicleRequest request = new CreateVehicleRequest();
        request.setLicencePlate("asdf-123");
        request.setProductionYear(2015);
        request.setType(VehicleTypes.CAR);
        return request;
    }

    private Vehicle getVehicleModel(){
        Vehicle vehicle = new Vehicle();
        ReflectionTestUtils.setField(vehicle, "id", 1L);
        vehicle.setLicencePlate("abc");
        vehicle.setProductionYear(2013);
        VehicleType type = new VehicleType();
        ReflectionTestUtils.setField(type, "id", VehicleTypes.CAR.getId());
        vehicle.setVehicleType(type);
        return vehicle;
    }
}