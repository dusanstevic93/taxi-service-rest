package com.dusan.taxiservice.web.api.rest.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mappings {

    // authentication controller
    public static final String AUTHENTICATION_BASE_PATH = "/authentication";
    
    // user registration controller
    public static final String REGISTRATION_BASE_PATH = "/registration";
    public static final String REGISTER_CLIENT = "/client";
    public static final String REGISTER_DRIVER = "/driver";
    
    // user profile controller
    public static final String PROFILE_BASE_PATH = "/profile";
    
    // driver controller
    public static final String DRIVER_BASE_PATH = "/drivers";
    public static final String UPDATE_LOCATION = "/current/location";
    public static final String SET_WAITING_STATUS = "/current/status/waiting-for-ride";
    public static final String SET_NOT_WORKING_STATUS = "/current/status/not-working";
    public static final String CURRENT_STATUS = "/current/status";
    
    // ride controller
    public static final String RETRIEVE_USER_RIDES = "/user/rides";
    public static final String RETRIEVE_ALL_RIDES = "/rides";
    public static final String RETRIEVE_ALL_CREATED_RIDES = "/rides/status/created";
    public static final String CREATE_RIDE = "/client/rides";
    public static final String UPDATE_RIDE = "/client/rides/{rideId}";
    public static final String CANCEL_RIDE = "/client/rides/{rideId}/status/cancel";
    public static final String RATE_RIDE = "/client/rides/{rideId}/rating";
    public static final String FORM_RIDE = "/dispatcher/rides";
    public static final String PROCESS_RIDE = "/dispatcher/rides/{rideId}";
    public static final String ACCEPT_RIDE = "/driver/rides/{rideId}";
    public static final String RIDE_STATUS_FAILED = "/driver/rides/{rideId}/status/failed";
    public static final String RIDE_STATUS_SUCCESSFUL = "/driver/rides/{rideId}/status/successful";
    
    
    // vehicle controller
    public static final String VEHICLE_BASE_PATH = "/vehicles";
    public static final String RETRIEVE_VEHICLE_DRIVERS = "/{vehicleId}/drivers"; 
    
    // ride comment controller
    public static final String RIDE_COMMENT = "/rides/{rideId}/comment";
    
    // ride report controller
    public static final String RIDE_REPORT = "/rides/{rideId}/report";

}
