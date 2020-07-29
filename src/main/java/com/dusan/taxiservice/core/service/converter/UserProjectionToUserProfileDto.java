package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.dao.projection.UserProjection;
import com.dusan.taxiservice.core.service.model.UserProfileDto;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class UserProjectionToUserProfileDto implements Converter<UserProjection, UserProfileDto> {

    @Override
    public UserProfileDto convert(UserProjection projection) {
        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(projection, dto);
        return dto;
    }
}
