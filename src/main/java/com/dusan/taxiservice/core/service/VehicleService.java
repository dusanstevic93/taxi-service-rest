package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.PageParams;
import com.dusan.taxiservice.core.service.model.CreateVehicleCommand;
import com.dusan.taxiservice.core.service.model.UpdateVehicleCommand;
import com.dusan.taxiservice.core.service.model.VehicleDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface VehicleService {

    void createVehicle(@Valid CreateVehicleCommand createCommand);
    VehicleDto findVehicle(long id);
    Page<VehicleDto> findAllVehicles(@Valid PageParams pageParams);
    void updateVehicle(@Valid UpdateVehicleCommand updateCommand);
}
