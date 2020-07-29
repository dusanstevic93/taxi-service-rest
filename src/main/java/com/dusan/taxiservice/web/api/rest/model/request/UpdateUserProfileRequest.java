package com.dusan.taxiservice.web.api.rest.model.request;

import com.dusan.taxiservice.core.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotBlank
    @Email
    private String email;
}
