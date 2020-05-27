package com.dusan.taxiservice.service;

import java.util.List;

import com.dusan.taxiservice.dto.request.CreateReportRequest;
import com.dusan.taxiservice.dto.request.CreateRideRequest;
import com.dusan.taxiservice.dto.request.FormRideRequest;
import com.dusan.taxiservice.dto.request.RidePageParams;
import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.dto.request.SuccessfulRideRequest;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.enums.UserRoles;

public interface RideService {

    void createRide(String clientUsername, CreateRideRequest createRideRequest);
    void updateRide(long rideId, String clientUsername, CreateRideRequest updateRideRequest);
    void cancelRide(long rideId, String clientUsername);
    void rateRide(long rideId, String clientUsername, int rating);
    void formRide(String dispatcherUsername, String driverUsername, FormRideRequest formRideRequest);
    void processRide(long rideId, String dispatcherUsername, String driverUsername);
    void acceptRide(long rideId, String driverUsername);
    void setFailedStatus(long rideId, String driverUsername, CreateReportRequest report);
    void setSuccessfulStatus(long rideId, String driverUsername, SuccessfulRideRequest successfulRideRequest);
    List<RideResponse> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams, RidePageParams pageParams);
    List<RideResponse> findAllRidesInCreatedStatus(RidePageParams pageParams);
    List<RideResponse> findAllRides(RideQueryParams queryParams, RidePageParams pageParams);
}
