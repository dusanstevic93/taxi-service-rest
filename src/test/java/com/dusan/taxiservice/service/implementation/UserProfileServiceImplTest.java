package com.dusan.taxiservice.service.implementation;

import com.dusan.taxiservice.dao.repository.UserRepository;
import com.dusan.taxiservice.dto.request.UpdateUserProfileRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.User;
import com.dusan.taxiservice.entity.enums.Gender;
import com.dusan.taxiservice.dao.projection.UserProjection;
import com.dusan.taxiservice.service.exception.EmailExistsException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void testGetProfileShouldBeSuccessful(){
        // given
        String username = "test";
        UserProjection testProjection = getUserProjectionModel();
        given(userRepository.getUserProjection(username)).willReturn(Optional.of(testProjection));

        // when
        UserProfileResponse profileResponse = userProfileService.getProfile(username);

        // then
        assertAll(
                () -> assertEquals(testProjection.getUsername(), profileResponse.getUsername()),
                () -> assertEquals(testProjection.getFirstName(), profileResponse.getFirstName()),
                () -> assertEquals(testProjection.getLastName(), profileResponse.getLastName()),
                () -> assertEquals(testProjection.getGender(), profileResponse.getGender()),
                () -> assertEquals(testProjection.getPhone(), profileResponse.getPhone()),
                () -> assertEquals(testProjection.getEmail(), profileResponse.getEmail())
        );

    }

    private UserProjection getUserProjectionModel(){
        UserProjection projection = new UserProjection();
        projection.setUsername("test");
        projection.setFirstName("test first name");
        projection.setLastName("test last name");
        projection.setPhone("0123");
        projection.setGender(Gender.MALE);
        projection.setEmail("test@mail.com");
        return projection;
    }

    @Test
    void testGetProfileUserNotFound(){
        // given
        String username = "test";
        given(userRepository.getUserProjection(username)).willReturn(Optional.empty());

        // when
        Executable attmptToRetrieveProfile = () -> userProfileService.getProfile(username);

        // then
        assertThrows(ResourceNotFoundException.class, attmptToRetrieveProfile);
    }

    @Test
    void testUpdateProfileShouldBeSuccessful(){
        // given
        String username = "test";
        UpdateUserProfileRequest testRequest = getUpdateUserProfileRequestModel();
        User userToUpdate = getUserModel();
        given(userRepository.findById(username)).willReturn(Optional.of(userToUpdate));

        // when
        userProfileService.updateProfile(username, testRequest);

        // then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(argumentCaptor.capture());
        User updatedUser = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(testRequest.getFirstName(), updatedUser.getFirstName()),
                () -> assertEquals(testRequest.getLastName(), updatedUser.getLastName()),
                () -> assertEquals(testRequest.getGender(), updatedUser.getGender()),
                () -> assertEquals(testRequest.getPhone(), updatedUser.getPhone()),
                () -> assertEquals(testRequest.getEmail(), updatedUser.getEmail())
        );
    }

    @Test
    void testUpdateProfileUserNotFound(){
        // given
        String username = "test";
        UpdateUserProfileRequest testRequest = getUpdateUserProfileRequestModel();
        given(userRepository.findById(username)).willReturn(Optional.empty());

        // when
        Executable attemptUpdate = () -> userProfileService.updateProfile(username, testRequest);

        // then
        assertThrows(ResourceNotFoundException.class, attemptUpdate);
    }

    @Test
    void testUpdateProfileNewEmailIsTaken(){
        // given
        String username = "test";
        UpdateUserProfileRequest testRequest = getUpdateUserProfileRequestModel();

        User userToUpdate = getUserModel();
        given(userRepository.findById(username)).willReturn(Optional.of(userToUpdate));

        given(userRepository.existsByEmail(testRequest.getEmail())).willReturn(true);

        // when
        Executable attemptUpdate = () -> userProfileService.updateProfile(username, testRequest);

        // then
        assertThrows(EmailExistsException.class, attemptUpdate);
    }

    private UpdateUserProfileRequest getUpdateUserProfileRequestModel(){
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setFirstName("updated first name");
        request.setLastName("updated last name");
        request.setGender(Gender.MALE);
        request.setPhone("123");
        request.setEmail("updated@mail.com");
        return request;
    }

    private User getUserModel(){
        User user = new Client();
        user.setFirstName("old first name");
        user.setLastName("old last name");
        user.setGender(Gender.FEMALE);
        user.setPhone("987");
        user.setEmail("old@mail.com");
        return user;
    }
}