package com.dusan.taxiservice.core.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import com.dusan.taxiservice.core.service.model.DriverDto;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dusan.taxiservice.core.dao.repository.DriverRepository;
import com.dusan.taxiservice.core.dao.repository.DriverStatusRepository;
import com.dusan.taxiservice.core.service.model.LocationDto;
import com.dusan.taxiservice.core.service.model.DriverQueryParams;
import com.dusan.taxiservice.core.service.model.PageParams;
import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.Location;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.dao.projection.UserProjection;
import com.dusan.taxiservice.core.dao.specification.DriverSpecification;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.core.service.DriverService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class DriverServiceImpl implements DriverService {
    
    private DriverRepository driverRepository;
    private DriverStatusRepository statusRepository;
    private ConversionService conversion;
    
    @Override
    public void updateLocation(String username, LocationDto locationDto) {
        Driver driver = getDriverFromDatabase(username);
        driver.setCurrentLocation(new Location(locationDto.getLatitude(), locationDto.getLongitude()));
        driverRepository.save(driver);
    }
    
    @Override
    @Transactional
    public void updateStatus(String username, DriverStatuses status) {
        driverRepository.updateStatus(username, status.getId());
        /*if (status == DriverStatuses.NOT_WORKING)
            driver.setCurrentLocation(null);*/
    }
    
    @Override
    public DriverStatuses getCurrentStatus(String username) {
        long statusId = driverRepository.getCurrentStatusId(username);
        return DriverStatuses.fromId(statusId);
    }
     
    private Driver getDriverFromDatabase(String username) {
        return driverRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }
    
    @Override
    public Page<DriverDto> findAllDrivers(DriverQueryParams queryParams, PageParams pageParams) {
        Specification<Driver> spec = new DriverSpecification(queryParams);      
        return getPage(spec, pageParams);
    }
       
    private Page<DriverDto> getPage(Specification<Driver> spec, PageParams pageParams){
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getLimit());
        Page<Driver> page = driverRepository.findAll(spec, pageable);
        return page.map(driver -> conversion.convert(driver, DriverDto.class));
    }

    @Override
    public List<UserProfileDto> findAllVehicleDrivers(long vehicleId) {
        List<UserProjection> drivers = driverRepository.findAllByVehicleId(vehicleId);
        return drivers.stream()
                .map(projection -> conversion.convert(projection, UserProfileDto.class))
                .collect(Collectors.toList());
    }
}
