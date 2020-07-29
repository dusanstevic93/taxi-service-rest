package com.dusan.taxiservice.core.service;

import com.dusan.taxiservice.core.service.model.CommentDto;
import com.dusan.taxiservice.core.service.model.CreateCommentCommand;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface RideCommentService {

    void createComment(@Valid CreateCommentCommand createCommand);
    CommentDto findComment(long rideId, String clientUsername);
}
