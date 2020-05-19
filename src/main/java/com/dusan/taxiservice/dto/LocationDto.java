package com.dusan.taxiservice.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocationDto {

    @NotNull
    @Min(-90)
    @Max(90)
    @Digits(integer = 2, fraction = 8)
    private Double latitude;
    
    @NotNull
    @Min(-180)
    @Max(180)
    @Digits(integer = 3, fraction = 8)
    private Double longitude;

}
