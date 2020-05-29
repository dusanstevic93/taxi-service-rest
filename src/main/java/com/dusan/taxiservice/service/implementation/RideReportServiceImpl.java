package com.dusan.taxiservice.service.implementation;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.DriverRepository;
import com.dusan.taxiservice.dao.RideReportRepository;
import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dto.request.CreateReportRequest;
import com.dusan.taxiservice.dto.response.ReportResponse;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideReport;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.RideReportService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideReportServiceImpl implements RideReportService {

    private RideReportRepository reportRepository;
    private RideRepository rideRepository;
    private DriverRepository driverRepository;
    private ConversionService conversion;
    
    @Override
    public void createReport(long rideId, String driverUsername, CreateReportRequest createReportRequest) {
        Ride ride = findRide(rideId, driverUsername);
        RideReport report = new RideReport();
        report.setDriver(driverRepository.getOne(driverUsername));
        report.setRide(ride);
        report.setReport(createReportRequest.getReport());
        reportRepository.save(report);
    }
    
    private Ride findRide(long rideId, String driverUsername) {
        Ride ride = rideRepository.findByIdAndDriver(rideId, driverRepository.getOne(driverUsername))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return ride;
    }

    @Override
    public ReportResponse getSpecificDriverReport(long rideId, String driverUsername) {
        Ride ride = rideRepository.getOne(rideId);
        Driver driver = driverRepository.getOne(driverUsername);
        RideReport report = reportRepository.findByRideAndDriver(ride, driver)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return conversion.convert(report, ReportResponse.class);
    }

    @Override
    public ReportResponse getAnyReport(long rideId) {
        Ride ride = rideRepository.getOne(rideId);
        RideReport report = reportRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return conversion.convert(report, ReportResponse.class);
    }
}
