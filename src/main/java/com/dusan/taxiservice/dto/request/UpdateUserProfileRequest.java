package com.dusan.taxiservice.dto.request;

import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dusan.taxiservice.entity.enums.Gender;

import lombok.Getter;

@Getter
@Setter
public class UpdateUserProfileRequest {

    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @NotNull
    private Gender gender;
    
    @NotBlank
    private String phone;
    
    @Email
    @NotBlank
    private String email;
    
}
