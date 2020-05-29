package com.dusan.taxiservice.service;

import com.dusan.taxiservice.dto.request.CreateReportRequest;
import com.dusan.taxiservice.dto.response.ReportResponse;

public interface RideReportService {

    void createReport(long rideId, String driverUsername, CreateReportRequest createReportRequest);
    ReportResponse getSpecificDriverReport(long rideId, String driverUsername);
    ReportResponse getAnyReport(long rideId);
}
