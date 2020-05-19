package com.dusan.taxiservice.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.dusan.taxiservice.entity.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CreateUserRequest {

    @Length(max = 30)
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    @Length(max = 30)
    @NotBlank
    private String firstName;
    
    @Length(max = 30)
    @NotBlank
    private String lastName;
    
    @NotNull
    private Gender gender;
    
    @Length(max = 30)
    @NotNull
    private String phone;
    
    @NotNull
    @Email
    private String email;
}
