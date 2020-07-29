package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.CreateDriverCommand;
import com.dusan.taxiservice.core.service.model.CreateUserCommand;
import com.dusan.taxiservice.core.service.model.UpdateUserProfileCommand;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface UserService {

    void createClient(@Valid CreateUserCommand createCommand);
    void createDriver(@Valid CreateDriverCommand createCommand);
    UserProfileDto getUserProfile(String username);
    void updateUserProfile(@Valid UpdateUserProfileCommand updateProfileCommand);
}
