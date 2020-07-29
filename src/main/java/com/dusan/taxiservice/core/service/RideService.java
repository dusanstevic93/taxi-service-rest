package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.RidePageParams;
import com.dusan.taxiservice.core.service.model.RideQueryParams;
import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.core.service.model.*;
import org.springframework.data.domain.Page;

public interface RideService {

    RideDto createRide(CreateRideCommand createCommand);
    RideDto updateRide(UpdateRideCommand updateCommand);
    RideDto cancelRide(CancelRideCommand cancelCommand);
    void rateRide(RateRideCommand rateCommand);
    RideDto formRide(FormRideCommand formCommand);
    RideDto processRide(ProcessRideCommand processCommand);
    RideDto acceptRide(AcceptRideCommand acceptCommand);
    RideDto setFailedStatus(SetFailedStatusCommand failedCommand);
    RideDto setSuccessfulStatus(SetSuccessfulStatusCommand successfulCommand);
    Page<RideDto> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams, RidePageParams pageParams);
    Page<RideDto> findAllRidesInCreatedStatus(RidePageParams pageParams);
    Page<RideDto> findAllRides(RideQueryParams queryParams, RidePageParams pageParams);
}
