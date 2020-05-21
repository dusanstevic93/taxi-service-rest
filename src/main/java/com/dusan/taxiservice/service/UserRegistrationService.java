package com.dusan.taxiservice.service;

import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Driver;

public interface UserRegistrationService {

    Client registerClient(CreateClientRequest createClientRequest);
    Driver registerDriver(CreateDriverRequest createDriverRequest);
}