package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dao.repository.DriverStatusRepository;
import com.dusan.taxiservice.dao.repository.RoleRepository;
import com.dusan.taxiservice.dao.repository.UserRepository;
import com.dusan.taxiservice.dao.repository.VehicleRepository;
import com.dusan.taxiservice.dto.request.CreateClientRequest;
import com.dusan.taxiservice.dto.request.CreateDriverRequest;
import com.dusan.taxiservice.entity.*;
import com.dusan.taxiservice.entity.enums.DriverStatuses;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.service.exception.EmailExistsException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.exception.UsernameExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverStatusRepository driverStatusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    void testRegisterClientShouldBeSuccessful(){
        // given
        CreateClientRequest testRequest = getCreateClientRequestModel();

        given(userRepository.existsById(testRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(testRequest.getEmail())).willReturn(false);
        given(passwordEncoder.encode(testRequest.getPassword())).willReturn("encodedPassword");
        UserRole role = new UserRole();
        ReflectionTestUtils.setField(role, "id", UserRoles.CLIENT.getId());
        given(roleRepository.getOne(UserRoles.CLIENT.getId())).willReturn(role);

        // when
        userRegistrationService.registerClient(testRequest);

        // then
        ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);
        then(userRepository).should().save(argumentCaptor.capture());
        Client registeredClient = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(testRequest.getUsername(), registeredClient.getUsername()),
                () -> assertEquals("encodedPassword", registeredClient.getPassword()),
                () -> assertEquals(testRequest.getFirstName(), registeredClient.getFirstName()),
                () -> assertEquals(testRequest.getLastName(), registeredClient.getLastName()),
                () -> assertEquals(testRequest.getGender(), registeredClient.getGender()),
                () -> assertEquals(testRequest.getPhone(), registeredClient.getPhone()),
                () -> assertEquals(testRequest.getEmail(), registeredClient.getEmail()),
                () -> assertEquals(UserRoles.CLIENT.getId(), registeredClient.getRole().getId())
        );
    }

    @Test
    void testRegisterClientUsernameNotUnique(){
        // given
        CreateClientRequest testRequest = getCreateClientRequestModel();
        given(userRepository.existsById(testRequest.getUsername())).willReturn(true);

        // when
        Executable attemptRegistration = () -> userRegistrationService.registerClient(testRequest);

        // then
        assertThrows(UsernameExistsException.class, attemptRegistration);
    }

    @Test
    void testRegisterClientEmailNotUnique(){
        // given
        CreateClientRequest testRequest = getCreateClientRequestModel();
        given(userRepository.existsByEmail(testRequest.getEmail())).willReturn(true);

        // when
        Executable attemptRegistration = () -> userRegistrationService.registerClient(testRequest);

        // then
        assertThrows(EmailExistsException.class, attemptRegistration);
    }

    private CreateClientRequest getCreateClientRequestModel(){
        CreateClientRequest testRequest = new CreateClientRequest();
        testRequest.setUsername("testUsername");
        testRequest.setPassword("testPassword");
        testRequest.setFirstName("testFirstName");
        testRequest.setLastName("testLastName");
        testRequest.setGender(Gender.FEMALE);
        testRequest.setPhone("testPhone");
        testRequest.setEmail("test@mail.com");
        return testRequest;
    }

    @Test
    void testRegisterDriverShouldBeSuccessful(){
        // given
        CreateDriverRequest testRequest = getCreateDriverRequestModel();

        given(userRepository.existsById(testRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(testRequest.getEmail())).willReturn(false);
        given(vehicleRepository.existsById(testRequest.getVehicleId())).willReturn(true);
        given(passwordEncoder.encode(testRequest.getPassword())).willReturn("encodedPassword");
        UserRole role = new UserRole();
        ReflectionTestUtils.setField(role, "id", UserRoles.DRIVER.getId());
        given(roleRepository.getOne(UserRoles.DRIVER.getId())).willReturn(role);
        DriverStatus status = new DriverStatus();
        ReflectionTestUtils.setField(status, "id", DriverStatuses.NOT_WORKING.getId());
        given(driverStatusRepository.getOne(DriverStatuses.NOT_WORKING.getId())).willReturn(status);
        Vehicle vehicle = new Vehicle();
        ReflectionTestUtils.setField(vehicle, "id", testRequest.getVehicleId());
        given(vehicleRepository.getOne(testRequest.getVehicleId())).willReturn(vehicle);

        // when
        userRegistrationService.registerDriver(testRequest);

        // then
        ArgumentCaptor<Driver> argumentCaptor = ArgumentCaptor.forClass(Driver.class);
        then(userRepository).should().save(argumentCaptor.capture());
        Driver registeredDriver = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(testRequest.getUsername(), registeredDriver.getUsername()),
                () -> assertEquals("encodedPassword", registeredDriver.getPassword()),
                () -> assertEquals(testRequest.getFirstName(), registeredDriver.getFirstName()),
                () -> assertEquals(testRequest.getLastName(), registeredDriver.getLastName()),
                () -> assertEquals(testRequest.getGender(), registeredDriver.getGender()),
                () -> assertEquals(testRequest.getPhone(), registeredDriver.getPhone()),
                () -> assertEquals(testRequest.getEmail(), registeredDriver.getEmail()),
                () -> assertEquals(testRequest.getVehicleId(), registeredDriver.getVehicle().getId()),
                () -> assertEquals(UserRoles.DRIVER.getId(), registeredDriver.getRole().getId())
        );

    }

    @Test
    void testRegisterDriverUsernameNotUnique(){
        // given
        CreateDriverRequest testRequest = new CreateDriverRequest();
        given(userRepository.existsById(testRequest.getUsername())).willReturn(true);

        // when
        Executable attemptRegistration = () -> userRegistrationService.registerDriver(testRequest);

        // then
        assertThrows(UsernameExistsException.class, attemptRegistration);
    }

    @Test
    void testRegisterDriverEmailNotUnique(){
        // given
        CreateDriverRequest testRequest = new CreateDriverRequest();
        given(userRepository.existsByEmail(testRequest.getEmail())).willReturn(true);

        // when
        Executable attemptRegistration = () -> userRegistrationService.registerDriver(testRequest);

        // then
        assertThrows(EmailExistsException.class, attemptRegistration);
    }

    @Test
    void testRegisterDriverVehicleNotFound(){
        // given
        CreateDriverRequest testRequest = getCreateDriverRequestModel();
        given(vehicleRepository.existsById(testRequest.getVehicleId())).willReturn(false);

        // when
        Executable attemptRegistration = () -> userRegistrationService.registerDriver(testRequest);

        // then
        assertThrows(ResourceNotFoundException.class, attemptRegistration);
    }

    private CreateDriverRequest getCreateDriverRequestModel(){
        CreateDriverRequest testRequest = new CreateDriverRequest();
        testRequest.setUsername("driverTest");
        testRequest.setPassword("password");
        testRequest.setFirstName("test");
        testRequest.setLastName("test");
        testRequest.setPhone("123");
        testRequest.setGender(Gender.MALE);
        testRequest.setEmail("driver@mail.com");
        testRequest.setVehicleId(1L);
        return testRequest;
    }
}