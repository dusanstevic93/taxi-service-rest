package com.dusan.taxiservice.entity.projection;

import com.dusan.taxiservice.entity.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProjection {

    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
    private String email;
}
