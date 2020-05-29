package com.dusan.taxiservice.service;

import java.util.List;

import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.Vehicle;

public interface VehicleService {

    void createVehicle(CreateVehicleRequest createVehicleRequest);
    VehicleResponse findVehicle(long id);
    List<VehicleResponse> findAllVehicles(PageParams pageParams);
    void updateVehicle(long vehicleId, CreateVehicleRequest updateRequest);
}
