package com.dusan.taxiservice.api.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Descriptions {

    private static final String DISPATCHER = "User needs to have DISPATCHER role to perform this operation.";
    private static final String DRIVER = "User needs to have DRIVER role to perform this operation";
    private static final String CLIENT = "User needs to have CLIENT role to perform this operation";
    
    // authentication controller
    public static final String AUTHENTICATE = "Attempt to authenticate user with provided username and password. "
            + "If authentication is successful, bearer token is set in response authorization header."; 
    
    // registration controller
    public static final String CREATE_CLIENT = "Create a new user with CLIENT role";
    public static final String CREATE_DRIVER = "Create a new user with DRIVER role. " + DISPATCHER;
    
    // user profile controller
    public static final String GET_USER_PROFILE = "Retrieve authenticated user profile information.";
    public static final String UPDATE_USER_PROFILE = "Update authenticated user profile information.";
    
    // vehicle controller
    public static final String CREATE_VEHICLE = "Creates a new vehicle. " + DISPATCHER;
    public static final String RETRIEVE_VEHICLES = "Retrieve all vehicles. " + DISPATCHER;
    public static final String RETRIEVE_VEHICLE = "Retrieve specific vehicle by id. " + DISPATCHER;
    public static final String UPDATE_VEHICLE = "Update specific vehicle by id. " + DISPATCHER;
    public static final String RETRIEVE_VEHICLE_DRIVERS = "Retrieves all drivers which drive specific vehicle. " + DISPATCHER;
    
    // driver controller
    public static final String RETRIEVE_DRIVERS = "Retrieve all drivers. " + DISPATCHER;
    public static final String UPDATE_LOCATION = "Update authenticated driver current location. " + DRIVER;
    public static final String SET_WAITING_STATUS = "Set authenticated driver status to waiting for ride. " + DRIVER;
    public static final String SET_NOT_WORKING_STATUS = "Set authenticated driver status to not working. " + DRIVER;
    public static final String CURRENT_STATUS = "Get current status of an authenticated driver. " + DRIVER;
    
    // login
    public static final String LOGIN = "If user is successfully authenticated, bearer token is returned in response authorization header.";
    
    // ride controller
    public static final String CREATE_RIDE = "Create new ride. " + CLIENT;
    public static final String UPDATE_RIDE = "Update created ride. " + CLIENT;
    public static final String CANCEL_RIDE = "Cancel created ride. " + CLIENT;
    public static final String FORM_RIDE = "Form new ride. " + DISPATCHER;
    public static final String PROCESS_RIDE = "Process created ride. " + DISPATCHER;
    public static final String ACCEPT_RIDE = "Accept created ride. " + DRIVER;
    public static final String SET_FAILED_STATUS = "Change status of a ride to failed. " + DRIVER;
    public static final String SET_SUCCESSFUL_STATUS = "Change status of a ride to successful. " + DRIVER;
    public static final String RETRIEVE_USER_RIDES = "Retrieve all rides in which authenticated user participated.";
    public static final String RETRIEVE_ALL_RIDES = "Retrieve all rides from the system. " + DISPATCHER;
    public static final String RETRIEVE_CREATED_RIDES = "Retrieve all rides in created status. " + DRIVER;
    public static final String RATE_RIDE = "Rate specific ride. " + CLIENT;
    
    // ride report controller
    public static final String GET_RIDE_REPORT = "Get ride report by ride id. Driver can see only own reports while dispatcher can see all reports.";
}
