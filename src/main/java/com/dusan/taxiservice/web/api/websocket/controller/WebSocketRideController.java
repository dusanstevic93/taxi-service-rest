package com.dusan.taxiservice.web.api.websocket.controller;

import com.dusan.taxiservice.core.service.RideService;
import com.dusan.taxiservice.core.service.model.*;
import com.dusan.taxiservice.web.api.websocket.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.security.Principal;

import static com.dusan.taxiservice.web.api.websocket.SocketDestinations.*;

@Controller
@AllArgsConstructor
public class WebSocketRideController {

    private RideService rideService;
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping(FORMED_RIDE)
    public void formRide(@Valid FormRideMessage message, Principal principal) {
        FormRideCommand formRideCommand = new FormRideCommand();
        formRideCommand.setDispatcherUsername(principal.getName());
        formRideCommand.setDriverUsername(message.getDriverUsername());
        BeanUtils.copyProperties(message, formRideCommand);
        RideDto formedRide = rideService.formRide(formRideCommand);
        messagingTemplate.convertAndSendToUser(message.getDriverUsername(), QUEUE_PREFIX + FORMED_RIDE, formedRide);
    }

    @MessageMapping(PROCESSED_RIDE)
    public void processRide(@Valid ProcessRideMessage message, Principal principal) {
        ProcessRideCommand processRideCommand = new ProcessRideCommand();
        processRideCommand.setDispatcherUsername(principal.getName());
        processRideCommand.setDriverUsername(message.getDriverUsername());
        processRideCommand.setRideId(message.getRideId());
        RideDto processedRide = rideService.processRide(processRideCommand);
        messagingTemplate.convertAndSendToUser(message.getDriverUsername(), QUEUE_PREFIX + PROCESSED_RIDE, processedRide);
    }

    @MessageMapping(ACCEPTED_RIDE)
    public RideDto acceptRide(AcceptRideMessage message, Principal principal) {
        AcceptRideCommand acceptRideCommand = new AcceptRideCommand();
        acceptRideCommand.setRideId(message.getRideId());
        acceptRideCommand.setDriverUsername(principal.getName());
        return rideService.acceptRide(acceptRideCommand);
    }

    @MessageMapping(FAILED_RIDE)
    public RideDto failedRide(@Valid FailedRideMessage message, Principal principal) {
        SetFailedStatusCommand failedCommand = new SetFailedStatusCommand();
        failedCommand.setRideId(message.getRideId());
        failedCommand.setDriverUsername(principal.getName());
        failedCommand.setReport(message.getReport());
        return rideService.setFailedStatus(failedCommand);
    }

    @MessageMapping(SUCCESSFUL_RIDE)
    public RideDto successfulRide(@Valid SuccessfulRideMessage message, Principal principal) {
        SetSuccessfulStatusCommand successfulCommand = new SetSuccessfulStatusCommand();
        successfulCommand.setDriverUsername(principal.getName());
        BeanUtils.copyProperties(message, successfulCommand);
        return rideService.setSuccessfulStatus(successfulCommand);
    }
}
