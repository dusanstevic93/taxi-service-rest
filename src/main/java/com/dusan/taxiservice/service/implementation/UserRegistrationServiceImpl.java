package com.dusan.taxiservice.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.DriverStatusRepository;
import com.dusan.taxiservice.dao.RoleRepository;
import com.dusan.taxiservice.dao.UserRepository;
import com.dusan.taxiservice.dao.VehicleRepository;
import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.exception.EmailExistsException;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.exception.UsernameExistsException;
import com.dusan.taxiservice.service.UserRegistrationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class UserRegistrationServiceImpl implements UserRegistrationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private VehicleRepository vehicleRepository;
    private DriverStatusRepository driverStatusRepository;
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Client registerClient(CreateClientRequest createClientRequest) {
        checkUsernameAndEmailUniqueness(createClientRequest.getUsername(), createClientRequest.getEmail());
        Client client = new Client(true);
        BeanUtils.copyProperties(createClientRequest, client, "password");
        String encodedPassword = passwordEncoder.encode(createClientRequest.getPassword());
        client.setPassword(encodedPassword);
        client.setRole(roleRepository.getOne(UserRoles.CLIENT.getId()));
        return userRepository.save(client);        
    }

    @Override
    public Driver registerDriver(CreateDriverRequest createDriverRequest) {
        checkUsernameAndEmailUniqueness(createDriverRequest.getUsername(), createDriverRequest.getEmail());
        checkIfVehicleExists(createDriverRequest.getVehicleId());
        Driver driver = new Driver(true);
        BeanUtils.copyProperties(createDriverRequest, driver, "password");
        String encodedPassword = passwordEncoder.encode(createDriverRequest.getPassword());
        driver.setPassword(encodedPassword);
        driver.setRole(roleRepository.getOne(UserRoles.DRIVER.getId()));
        driver.setStatus(driverStatusRepository.getOne(DriverStatuses.NOT_WORKING.getId()));
        driver.setVehicle(vehicleRepository.getOne(createDriverRequest.getVehicleId()));
        return userRepository.save(driver);      
    }
    
    private void checkIfVehicleExists(long vehicleId) {
        boolean exists = vehicleRepository.existsById(vehicleId);
        if (!exists)
            throw new ResourceNotFoundException("Vehicle with id = " + vehicleId + " does not exists");
    }
    
    private void checkUsernameAndEmailUniqueness(String username, String email) {
        if (userRepository.existsById(username))
            throw new UsernameExistsException("Username is taken");
        if (userRepository.existsByEmail(email))
            throw new EmailExistsException("Email is taken");
    }
}
