package com.dusan.taxiservice.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.Vehicle;
import com.dusan.taxiservice.entity.enums.VehicleTypes;

public class VehicleToVehicleResponse implements Converter<Vehicle, VehicleResponse> {

    @Override
    public VehicleResponse convert(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        BeanUtils.copyProperties(vehicle, response, "vehicleType");
        response.setVehicleType(VehicleTypes.fromId(vehicle.getVehicleType().getId()));
        return response;
    }

}
