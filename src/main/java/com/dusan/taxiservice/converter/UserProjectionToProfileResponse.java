package com.dusan.taxiservice.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.response.UserProfileResponse;
import com.dusan.taxiservice.entity.projection.UserProjection;

public class UserProjectionToProfileResponse implements Converter<UserProjection, UserProfileResponse> {

    @Override
    public UserProfileResponse convert(UserProjection source) {
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(source, response);
        return response;
    }

}
