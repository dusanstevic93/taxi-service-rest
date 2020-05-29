package com.dusan.taxiservice;

import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.entity.enums.Gender;
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
}
