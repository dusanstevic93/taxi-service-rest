package com.dusan.taxiservice.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dusan.taxiservice.dto.request.CreateReportRequest;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.dto.request.FormRideRequest;
import com.dusan.taxiservice.dto.request.RidePageParams;
import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.dto.request.SuccessfulRideRequest;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.service.RideReportService;
import com.dusan.taxiservice.service.DriverService;
import com.dusan.taxiservice.service.RideService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideServiceImp implements RideService {

    private ClientRideRequestProcessor clientRequestProcessor;
    private DispatcherRideRequestProcessor dispatcherRequestProcessor;
    private DriverRideRequestProcessor driverRequestProcessor;
    private RideFinder finder;
    private RideReportService reportService;
    private DriverService driverService;
    
    
    @Override
    public RideResponse createRide(String clientUsername, CreateRideRequest createRideRequest) {
        return clientRequestProcessor.createRide(clientUsername, createRideRequest);
    }

    @Override
    @Transactional
    public RideResponse updateRide(long rideId, String clientUsername, CreateRideRequest updateRideRequest) {
        return clientRequestProcessor.updateRide(rideId, clientUsername, updateRideRequest);
    }

    @Override
    @Transactional
    public RideResponse cancelRide(long rideId, String clientUsername) {
        return clientRequestProcessor.cancelRide(rideId, clientUsername);
    }
    
    @Override
    public void rateRide(long rideId, String clientUsername, int rating) {
        clientRequestProcessor.rateRide(rideId, clientUsername, rating);
    }

    @Override
    @Transactional
    public RideResponse formRide(String dispatcherUsername, String driverUsername, FormRideRequest formRideRequest) {
        RideResponse formedRide = dispatcherRequestProcessor.formRide(dispatcherUsername, driverUsername, formRideRequest);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
        return formedRide;
    }

    @Override
    @Transactional
    public RideResponse processRide(long rideId, String dispatcherUsername, String driverUsername) {
        RideResponse processedRide = dispatcherRequestProcessor.processRide(rideId, dispatcherUsername, driverUsername);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
        return processedRide;
    }

    @Override
    @Transactional
    public RideResponse acceptRide(long rideId, String driverUsername) {
        RideResponse acceptedRide = driverRequestProcessor.acceptRide(rideId, driverUsername);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
        return acceptedRide;
    }

    @Override
    @Transactional
    public RideResponse setFailedStatus(long rideId, String driverUsername, CreateReportRequest report) {
        RideResponse failedRide = driverRequestProcessor.setFailedStatus(rideId, driverUsername);
        reportService.createReport(rideId, driverUsername, report);
        driverService.updateStatus(driverUsername, DriverStatuses.WAITING_FOR_RIDE);
        return failedRide;
    }

    @Override
    @Transactional
    public RideResponse setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest) {
        RideResponse successfulRide = driverRequestProcessor.setSuccessfulStatus(rideId, driverUsername, successfulRideRequest);
        driverService.updateStatus(driverUsername, DriverStatuses.WAITING_FOR_RIDE);
        return successfulRide;
    }

    @Override
    public List<RideResponse> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams,
            RidePageParams pageParams) {
        return finder.findAllRidesOfSpecificUser(username, userRole, queryParams, pageParams);
    }

    @Override
    public List<RideResponse> findAllRidesInCreatedStatus(RidePageParams pageParams) {
        return finder.findAllRidesInCreatedStatus(pageParams);
    }

    @Override
    public List<RideResponse> findAllRides(RideQueryParams queryParams, RidePageParams pageParams) {
        return finder.findAllRides(queryParams, pageParams);
    }
}
