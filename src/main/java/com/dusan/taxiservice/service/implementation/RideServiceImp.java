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
    public void createRide(String clientUsername, CreateRideRequest createRideRequest) {
        clientRequestProcessor.createRide(clientUsername, createRideRequest);
    }

    @Override
    @Transactional
    public void updateRide(long rideId, String clientUsername, CreateRideRequest updateRideRequest) {
        clientRequestProcessor.updateRide(rideId, clientUsername, updateRideRequest);
    }

    @Override
    @Transactional
    public void cancelRide(long rideId, String clientUsername) {
        clientRequestProcessor.cancelRide(rideId, clientUsername);
    }
    
    @Override
    public void rateRide(long rideId, String clientUsername, int rating) {
        clientRequestProcessor.rateRide(rideId, clientUsername, rating);
    }

    @Override
    @Transactional
    public void formRide(String dispatcherUsername, String driverUsername, FormRideRequest formRideRequest) {
        dispatcherRequestProcessor.formRide(dispatcherUsername, driverUsername, formRideRequest);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
    }

    @Override
    @Transactional
    public void processRide(long rideId, String dispatcherUsername, String driverUsername) {
        dispatcherRequestProcessor.processRide(rideId, dispatcherUsername, driverUsername);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
    }

    @Override
    @Transactional
    public void acceptRide(long rideId, String driverUsername) {
        driverRequestProcessor.acceptRide(rideId, driverUsername);
        driverService.updateStatus(driverUsername, DriverStatuses.ON_RIDE);
    }

    @Override
    @Transactional
    public void setFailedStatus(long rideId, String driverUsername, CreateReportRequest report) {
        driverRequestProcessor.setFailedStatus(rideId, driverUsername);
        reportService.createReport(rideId, driverUsername, report);
        driverService.updateStatus(driverUsername, DriverStatuses.WAITING_FOR_RIDE);
    }

    @Override
    @Transactional
    public void setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest) {
        driverRequestProcessor.setSuccessfulStatus(rideId, driverUsername, successfulRideRequest);
        driverService.updateStatus(driverUsername, DriverStatuses.WAITING_FOR_RIDE);
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
