package com.dusan.taxiservice.web.api.rest.model.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.dusan.taxiservice.core.entity.enums.VehicleTypes;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRideRequest {

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

    @NotNull
    private VehicleTypes vehicleType = VehicleTypes.CAR;   
}
