package com.dusan.taxiservice.dto.response;

import com.dusan.taxiservice.entity.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {

    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
    private String email;
}
