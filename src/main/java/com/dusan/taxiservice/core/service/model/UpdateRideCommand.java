package com.dusan.taxiservice.core.service.model;

import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateRideCommand {

    private long rideId;

    @NotBlank
    private String clientUsername;

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
