package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateReportCommand {

    private long rideId;

    @NotBlank
    private String driverUsername;

    @NotBlank
    private String report;
}
