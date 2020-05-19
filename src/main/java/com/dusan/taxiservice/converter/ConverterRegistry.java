package com.dusan.taxiservice.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterRegistry implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {       
        
        VehicleToVehicleResponse vehicleToVehicleResponse = new VehicleToVehicleResponse();
        registry.addConverter(vehicleToVehicleResponse);
        
        DriverToDriverResponse driverToDriverResponse = new DriverToDriverResponse(vehicleToVehicleResponse);
        registry.addConverter(driverToDriverResponse);
        
        RideToRideResponse rideToRideResponse = new RideToRideResponse();
        registry.addConverter(rideToRideResponse);
        
        CommentToCommentResponse commentToCommentResponse = new CommentToCommentResponse();
        registry.addConverter(commentToCommentResponse);
        
        ReportToReportResponse reportToReportResponse = new ReportToReportResponse();
        registry.addConverter(reportToReportResponse);
        
        UserProjectionToProfileResponse userProjectionToProfileResponse = new UserProjectionToProfileResponse();
        registry.addConverter(userProjectionToProfileResponse);
    }
}
