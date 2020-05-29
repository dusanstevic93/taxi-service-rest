package com.dusan.taxiservice.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.VehicleRepository;
import com.dusan.taxiservice.dao.VehicleTypeRepository;
import com.dusan.taxiservice.dto.request.CreateVehicleRequest;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.VehicleResponse;
import com.dusan.taxiservice.entity.Vehicle;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.VehicleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleTypeRepository vehicleTypeRepository;
    private ConversionService conversion;
    
    @Override
    public void createVehicle(CreateVehicleRequest createVehicleRequest) {
        Vehicle vehicle = new Vehicle();
        vehicle.setProductionYear(createVehicleRequest.getProductionYear());
        vehicle.setLicencePlate(createVehicleRequest.getLicencePlate());
        vehicle.setVehicleType(vehicleTypeRepository.getOne(createVehicleRequest.getType().getId()));
        vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleResponse findVehicle(long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return conversion.convert(vehicle, VehicleResponse.class);
    }

    @Override
    public List<VehicleResponse> findAllVehicles(PageParams pageParams) {
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getLimit());
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        List<VehicleResponse> response = vehicles.getContent().stream()
                .map(vehicle -> conversion.convert(vehicle, VehicleResponse.class))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public void updateVehicle(long vehicleId, CreateVehicleRequest updateRequest) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle not found"));
        vehicle.setProductionYear(updateRequest.getProductionYear());
        vehicle.setLicencePlate(updateRequest.getLicencePlate());
        vehicle.setVehicleType(vehicleTypeRepository.getOne(updateRequest.getType().getId()));
        vehicleRepository.save(vehicle);
    }
}
