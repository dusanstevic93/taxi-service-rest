package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.dao.projection.UserProjection;
import com.dusan.taxiservice.core.dao.repository.DriverStatusRepository;
import com.dusan.taxiservice.core.dao.repository.RoleRepository;
import com.dusan.taxiservice.core.dao.repository.UserRepository;
import com.dusan.taxiservice.core.dao.repository.VehicleRepository;
import com.dusan.taxiservice.core.entity.Client;
import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.User;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.core.service.UserService;
import com.dusan.taxiservice.core.service.exception.EmailExistsException;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.core.service.exception.UsernameExistsException;
import com.dusan.taxiservice.core.service.model.CreateDriverCommand;
import com.dusan.taxiservice.core.service.model.CreateUserCommand;
import com.dusan.taxiservice.core.service.model.UpdateUserProfileCommand;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private VehicleRepository vehicleRepository;
    private DriverStatusRepository driverStatusRepository;

    @Override
    public void createClient(CreateUserCommand createCommand) {
        checkUsernameUniqueness(createCommand.getUsername());
        checkEmailUniqueness(createCommand.getEmail());
        Client client = new Client(true);
        BeanUtils.copyProperties(createCommand, client);
        client.setRole(roleRepository.getOne(UserRoles.CLIENT.getId()));
        userRepository.save(client);
    }

    @Override
    public void createDriver(CreateDriverCommand createCommand) {
        checkUsernameUniqueness(createCommand.getUsername());
        checkEmailUniqueness(createCommand.getEmail());
        checkIfVehicleExists(createCommand.getVehicleId());
        Driver driver = new Driver(true);
        BeanUtils.copyProperties(createCommand, driver);
        driver.setRole(roleRepository.getOne(UserRoles.DRIVER.getId()));
        driver.setStatus(driverStatusRepository.getOne(DriverStatuses.NOT_WORKING.getId()));
        driver.setVehicle(vehicleRepository.getOne(createCommand.getVehicleId()));
        userRepository.save(driver);
    }

    private void checkUsernameUniqueness(String username) {
        if (userRepository.existsById(username))
            throw new UsernameExistsException("Username is taken");
    }

    private void checkIfVehicleExists(long vehicleId) {
        boolean exists = vehicleRepository.existsById(vehicleId);
        if (!exists)
            throw new ResourceNotFoundException("Vehicle with id = " + vehicleId + " does not exists");
    }

    @Override
    public UserProfileDto getUserProfile(String username) {
        UserProjection userProjection = userRepository.getUserProjection(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfileDto profileDto = new UserProfileDto();
        BeanUtils.copyProperties(userProjection, profileDto);
        return profileDto;
    }

    @Override
    public void updateUserProfile(UpdateUserProfileCommand updateProfileCommand) {
        User user = getUserFromDatabase(updateProfileCommand.getUsername());

        // if user is updating email, check if new email is unique
        boolean isUpdatingEmail = !user.getEmail().equals(updateProfileCommand.getEmail());
        if (isUpdatingEmail)
            checkEmailUniqueness(updateProfileCommand.getEmail());

        BeanUtils.copyProperties(updateProfileCommand, user, "username");
        userRepository.save(user);
    }

    private User getUserFromDatabase(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void checkEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailExistsException("Email is taken");
    }
}
