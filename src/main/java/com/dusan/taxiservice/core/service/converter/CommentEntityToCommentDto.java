package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.entity.RideComment;
import com.dusan.taxiservice.core.service.model.CommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class CommentEntityToCommentDto implements Converter<RideComment, CommentDto> {

    @Override
    public CommentDto convert(RideComment source) {
        CommentDto dto = new CommentDto();
        dto.setId(source.getId());
        dto.setCreationDateTime(source.getCreationDateTime());
        dto.setClientUsername(source.getClient().getUsername());
        dto.setRideId(source.getRide().getId());
        dto.setComment(source.getComment());
        return dto;
    }
}
