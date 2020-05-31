package com.dusan.taxiservice.model;

import com.dusan.taxiservice.dto.request.*;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestModels {

    public static UpdateUserProfileRequest getUpdateUserProfileRequest() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setFirstName("first name test");
        request.setLastName("last name test");
        request.setEmail("test@mail.com");
        request.setPhone("0123");
        request.setGender(Gender.MALE);
        return request;
    }

    public static CreateClientRequest getCreateClientRequestModel() {
        CreateClientRequest testRequest = new CreateClientRequest();
        testRequest.setUsername("clientTest");
        testRequest.setPassword("password");
        testRequest.setFirstName("test");
        testRequest.setLastName("test");
        testRequest.setPhone("123");
        testRequest.setGender(Gender.FEMALE);
        testRequest.setEmail("client@mail.com");
        return testRequest;
    }

    public static CreateDriverRequest getCreateDriverRequestModel() {
        CreateDriverRequest testRequest = new CreateDriverRequest();
        testRequest.setUsername("driverTest");
        testRequest.setPassword("password");
        testRequest.setFirstName("test");
        testRequest.setLastName("test");
        testRequest.setPhone("123");
        testRequest.setGender(Gender.MALE);
        testRequest.setEmail("driver@mail.com");
        testRequest.setVehicleId(1L);
        return testRequest;
    }

    public static CreateVehicleRequest getCreateVehicleRequestModel() {
        CreateVehicleRequest request = new CreateVehicleRequest();
        request.setLicencePlate("asdf-123");
        request.setProductionYear(2015);
        request.setType(VehicleTypes.CAR);
        return request;
    }

    public static LoginRequest getLoginRequestModel() {
        LoginRequest request = new LoginRequest();
        request.setUsername("usernameTest");
        request.setPassword("passwordTest");
        return request;
    }
}
