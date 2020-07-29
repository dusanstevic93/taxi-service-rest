package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.service.model.RidePageParams;
import com.dusan.taxiservice.core.service.model.RideQueryParams;
import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.core.service.model.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.dusan.taxiservice.core.service.RideService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideServiceImp implements RideService {

    private ClientRideCommandExecutor clientCommandExecutor;
    private DispatcherRideCommandExecutor dispatcherCommandExecutor;
    private DriverRideCommandExecutor driverCommandExecutor;
    private RideFinder finder;


    @Override
    public RideDto createRide(CreateRideCommand createCommand) {
        return clientCommandExecutor.createRide(createCommand);
    }

    @Override
    public RideDto updateRide(UpdateRideCommand updateCommand) {
        return clientCommandExecutor.updateRide(updateCommand);
    }

    @Override
    public RideDto cancelRide(CancelRideCommand cancelCommand) {
        return clientCommandExecutor.cancelRide(cancelCommand);
    }

    @Override
    public void rateRide(RateRideCommand rateCommand) {
        clientCommandExecutor.rateRide(rateCommand);
    }

    @Override
    public RideDto formRide(FormRideCommand formCommand) {
        return dispatcherCommandExecutor.formRide(formCommand);
    }

    @Override
    public RideDto processRide(ProcessRideCommand processCommand) {
        return dispatcherCommandExecutor.processRide(processCommand);
    }

    @Override
    public RideDto acceptRide(AcceptRideCommand acceptCommand) {
        return driverCommandExecutor.acceptRide(acceptCommand);
    }

    @Override
    public RideDto setFailedStatus(SetFailedStatusCommand failedCommand) {
        return driverCommandExecutor.setFailedStatus(failedCommand);
    }

    @Override
    public RideDto setSuccessfulStatus(SetSuccessfulStatusCommand successfulCommand) {
        return driverCommandExecutor.setSuccessfulStatus(successfulCommand);
    }

    @Override
    public Page<RideDto> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams, RidePageParams pageParams) {
        return finder.findAllRidesOfSpecificUser(username, userRole, queryParams, pageParams);
    }

    @Override
    public Page<RideDto> findAllRidesInCreatedStatus(RidePageParams pageParams) {
        return finder.findAllRidesInCreatedStatus(pageParams);
    }

    @Override
    public Page<RideDto> findAllRides(RideQueryParams queryParams, RidePageParams pageParams) {
        return finder.findAllRides(queryParams, pageParams);
    }
}
