package com.dusan.taxiservice.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.UserRepository;
import com.dusan.taxiservice.dto.request.UpdateUserProfileRequest;
import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.User;
import com.dusan.taxiservice.entity.projection.UserProjection;
import com.dusan.taxiservice.exception.EmailExistsException;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.UserProfileService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class UserProfileServiceImpl implements UserProfileService {
    
    private UserRepository userRepository;
    
    @Override
    public UserProfileResponse getProfile(String username) {
        UserProjection userProjection = userRepository.getUserProjection(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfileResponse profile = new UserProfileResponse();
        BeanUtils.copyProperties(userProjection, profile);
        return profile;
    }

    @Override
    public void updateProfile(String username, UpdateUserProfileRequest updateProfileRequest) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        boolean isUpdatingEmail = !user.getEmail().equals(updateProfileRequest.getEmail());
        if (isUpdatingEmail)
            checkEmailUniqueness(updateProfileRequest.getEmail());
        BeanUtils.copyProperties(updateProfileRequest, user);
        userRepository.save(user);
    }
    
    private void checkEmailUniqueness(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists)
            throw new EmailExistsException("Email is taken"); 
    }
}
