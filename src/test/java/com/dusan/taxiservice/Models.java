package com.dusan.taxiservice;

import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.request.*;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Models {

    public static UserProfileResponse getUserProfileResponseModel() {
        UserProfileResponse response = new UserProfileResponse();
        response.setUsername("username test");
        response.setFirstName("first name test");
        response.setLastName("last name test");
        response.setEmail("test@mail.com");
        response.setPhone("0123");
        response.setGender(Gender.MALE);
        return response;
    }

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

    public static VehicleResponse getVehicleResponseModel(){
        VehicleResponse response = new VehicleResponse();
        response.setId(1L);
        response.setVehicleType(VehicleTypes.CAR);
        response.setLicencePlate("aaa-111");
        response.setProductionYear(2019);
        return  response;
    }

    public static LocationDto getLocationDtoModel() {
        LocationDto location = new LocationDto(25.85, 13.26);
        return location;
    }

    public static LoginRequest getLoginRequestModel() {
        LoginRequest request = new LoginRequest();
        request.setUsername("usernameTest");
        request.setPassword("passwordTest");
        return request;
    }
}
