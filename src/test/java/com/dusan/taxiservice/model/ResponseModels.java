package com.dusan.taxiservice.model;

import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.entity.enums.VehicleTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseModels {

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

    public static VehicleResponse getVehicleResponseModel(){
        VehicleResponse response = new VehicleResponse();
        response.setId(1L);
        response.setVehicleType(VehicleTypes.CAR);
        response.setLicencePlate("aaa-111");
        response.setProductionYear(2019);
        return  response;
    }
}
