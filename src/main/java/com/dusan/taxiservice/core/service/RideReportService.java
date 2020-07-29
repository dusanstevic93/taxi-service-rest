package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.CreateReportCommand;
import com.dusan.taxiservice.core.service.model.ReportDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface RideReportService {

    void createReport(@Valid CreateReportCommand createCommand);
    ReportDto getSpecificDriverReport(long rideId, String driverUsername);
    ReportDto getAnyReport(long rideId);
}
