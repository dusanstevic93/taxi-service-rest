package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.entity.Vehicle;
import com.dusan.taxiservice.core.entity.enums.VehicleTypes;
import com.dusan.taxiservice.core.service.model.VehicleDto;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class VehicleEntityToVehicleDto implements Converter<Vehicle, VehicleDto> {

    @Override
    public VehicleDto convert(Vehicle entity) {
        VehicleDto dto = new VehicleDto();
        BeanUtils.copyProperties(entity, dto, "vehicleType");
        dto.setVehicleType(VehicleTypes.fromId(entity.getVehicleType().getId()));
        return dto;
    }
}
