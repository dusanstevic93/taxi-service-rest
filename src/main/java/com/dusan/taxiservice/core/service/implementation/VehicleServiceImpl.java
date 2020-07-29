package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.service.model.CreateVehicleCommand;
import com.dusan.taxiservice.core.service.model.UpdateVehicleCommand;
import com.dusan.taxiservice.core.service.model.VehicleDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.VehicleRepository;
import com.dusan.taxiservice.core.dao.repository.VehicleTypeRepository;
import com.dusan.taxiservice.core.service.model.PageParams;
import com.dusan.taxiservice.core.entity.Vehicle;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.core.service.VehicleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class VehicleServiceImpl implements VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private ConversionService conversion;
    
    @Override
    public void createVehicle(CreateVehicleCommand createCommand) {
        Vehicle vehicle = new Vehicle();
        vehicle.setProductionYear(createCommand.getProductionYear());
        vehicle.setLicencePlate(createCommand.getLicencePlate());
        vehicle.setVehicleType(vehicleTypeRepository.getOne(createCommand.getType().getId()));
        vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleDto findVehicle(long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return conversion.convert(vehicle, VehicleDto.class);
    }

    @Override
    public Page<VehicleDto> findAllVehicles(PageParams pageParams) {
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getLimit());
        Page<Vehicle> page = vehicleRepository.findAll(pageable);
        return page.map(vehicle -> conversion.convert(vehicle, VehicleDto.class));
    }

    @Override
    public void updateVehicle(UpdateVehicleCommand updateCommand) {
        Vehicle vehicle = vehicleRepository.findById(updateCommand.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("vehicle not found"));
        vehicle.setProductionYear(updateCommand.getProductionYear());
        vehicle.setLicencePlate(updateCommand.getLicencePlate());
        vehicle.setVehicleType(vehicleTypeRepository.getOne(updateCommand.getType().getId()));
        vehicleRepository.save(vehicle);
    }
}
