package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.service.model.CreateReportCommand;
import com.dusan.taxiservice.core.service.model.ReportDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.DriverRepository;
import com.dusan.taxiservice.core.dao.repository.RideReportRepository;
import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.RideReport;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.core.service.RideReportService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideReportServiceImpl implements RideReportService {

    private RideReportRepository reportRepository;
    private RideRepository rideRepository;
    private DriverRepository driverRepository;
    private ConversionService conversion;
    
    @Override
    public void createReport(CreateReportCommand createCommand) {
        Ride ride = getRideFromDatabase(createCommand.getRideId(), createCommand.getDriverUsername());
        RideReport rideReport = new RideReport();
        rideReport.setDriver(driverRepository.getOne(createCommand.getDriverUsername()));
        rideReport.setRide(ride);
        rideReport.setReport(createCommand.getReport());
        reportRepository.save(rideReport);
    }
    
    private Ride getRideFromDatabase(long rideId, String driverUsername) {
        return rideRepository.findByIdAndDriver(rideId, driverRepository.getOne(driverUsername))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }

    @Override
    public ReportDto getSpecificDriverReport(long rideId, String driverUsername) {
        Ride ride = rideRepository.getOne(rideId);
        Driver driver = driverRepository.getOne(driverUsername);
        RideReport report = reportRepository.findByRideAndDriver(ride, driver)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return conversion.convert(report, ReportDto.class);
    }

    @Override
    public ReportDto getAnyReport(long rideId) {
        Ride ride = rideRepository.getOne(rideId);
        RideReport report = reportRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return conversion.convert(report, ReportDto.class);
    }
}
