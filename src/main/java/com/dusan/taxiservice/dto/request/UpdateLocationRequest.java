package com.dusan.taxiservice.dto.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLocationRequest {

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;
    
    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;
}
