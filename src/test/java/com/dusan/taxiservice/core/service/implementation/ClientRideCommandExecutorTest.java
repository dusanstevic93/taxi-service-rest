package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.dao.repository.ClientRepository;
import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.dao.repository.RideStatusRepository;
import com.dusan.taxiservice.core.dao.repository.VehicleTypeRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientRideCommandExecutorTest {

    private @Mock RideRepository rideRepository;
    private @Mock RideStatusRepository rideStatusRepository;
    private @Mock VehicleTypeRepository vehicleTypeRepository;
    private @Mock ClientRepository clientRepository;
    private @Mock ConversionService conversionService;
    private @InjectMocks ClientRideCommandExecutor commandExecutor;


}