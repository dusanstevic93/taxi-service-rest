package com.dusan.taxiservice.converter;

import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.response.CommentResponse;
import com.dusan.taxiservice.entity.RideComment;

public class CommentToCommentResponse implements Converter<RideComment, CommentResponse> {

    @Override
    public CommentResponse convert(RideComment source) {
        CommentResponse response = new CommentResponse();
        response.setId(source.getId());
        response.setCreationDateTime(source.getCreationDateTime());
        response.setClientUsername(source.getClient().getUsername());
        response.setRideId(source.getRide().getId());
        response.setComment(source.getComment());
        return response;
    }
}
