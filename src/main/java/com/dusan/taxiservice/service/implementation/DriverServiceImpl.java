package com.dusan.taxiservice.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dusan.taxiservice.dao.DriverRepository;
import com.dusan.taxiservice.dao.DriverStatusRepository;
import com.dusan.taxiservice.dto.LocationDto;
import com.dusan.taxiservice.dto.request.DriverQueryParams;
import com.dusan.taxiservice.dto.request.PageParams;
import com.dusan.taxiservice.dto.response.DriverResponse;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Location;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.projection.UserProjection;
import com.dusan.taxiservice.entity.specification.DriverSpecification;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.DriverService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class DriverServiceImpl implements DriverService {
    
    private DriverRepository driverRepository;
    private DriverStatusRepository statusRepository;
    private ConversionService conversion;
    
    @Override
    public Driver updateLocation(String username, LocationDto locationDto) {
        Driver driver = findDriver(username);
        driver.setCurrentLocation(new Location(locationDto.getLatitude(), locationDto.getLongitude()));
        return driverRepository.save(driver);       
    }
    
    @Override
    @Transactional
    public Driver updateStatus(String username, DriverStatuses status) {
        driverRepository.updateStatus(username, status.getId());
        /*if (status == DriverStatuses.NOT_WORKING)
            driver.setCurrentLocation(null);*/
        return null;
    }
    
    @Override
    public DriverStatuses getCurrentStatus(String username) {
        long statusId = driverRepository.getCurrentStatusId(username);
        return DriverStatuses.fromId(statusId);
    }
     
    private Driver findDriver(String username) {
        Driver driver = driverRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        return driver;
    }
    
    @Override
    public List<DriverResponse> findAllDrivers(DriverQueryParams queryParams, PageParams pageParams) {
        Specification<Driver> spec = new DriverSpecification(queryParams);      
        return getResponseList(spec, pageParams);      
    }
       
    private List<DriverResponse> getResponseList(Specification<Driver> spec, PageParams pageParams){
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getLimit());
        Page<Driver> page = driverRepository.findAll(spec, pageable);
        return createResponse(page);
    }
    
    private List<DriverResponse> createResponse(Page<Driver> page){
        List<DriverResponse> responseList = new ArrayList<>();
        page.getContent().forEach(driver -> responseList.add(conversion.convert(driver, DriverResponse.class)));
        return responseList;
    }

    @Override
    public List<UserProfileResponse> findAllVehicleDrivers(long vehicleId) {
        List<UserProjection> drivers = driverRepository.findAllByVehicleId(vehicleId);
        List<UserProfileResponse> response = drivers.stream()
                .map(driver -> conversion.convert(driver, UserProfileResponse.class))
                .collect(Collectors.toList());
        return response;
    }
}
