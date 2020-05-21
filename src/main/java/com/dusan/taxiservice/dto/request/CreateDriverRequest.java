package com.dusan.taxiservice.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDriverRequest extends CreateUserRequest {

    @NotNull
    private Long vehicleId;
}
