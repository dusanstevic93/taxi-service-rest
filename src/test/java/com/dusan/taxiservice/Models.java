package com.dusan.taxiservice;

import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Models {

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
}
