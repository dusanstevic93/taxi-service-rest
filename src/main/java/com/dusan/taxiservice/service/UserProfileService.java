package com.dusan.taxiservice.service;

import com.dusan.taxiservice.dto.request.UpdateUserProfileRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.User;

public interface UserProfileService {

    UserProfileResponse getProfile(String username);
    void updateProfile(String username, UpdateUserProfileRequest updateProfileRequest);
}
