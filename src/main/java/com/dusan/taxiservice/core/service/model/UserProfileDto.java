package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {

    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
    private String email;
}
