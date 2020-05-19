package com.dusan.taxiservice.dto.response;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class ReportResponse {

    private long id;
    private long rideId;
    private String driverUsername;
    private String report;
}
