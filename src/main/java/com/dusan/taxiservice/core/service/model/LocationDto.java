package com.dusan.taxiservice.core.service.model;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocationDto {

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    @Digits(integer = 2, fraction = 8)
    private BigDecimal latitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    @Digits(integer = 3, fraction = 8)
    private BigDecimal longitude;
}
