package com.dusan.taxiservice.service;

import com.dusan.taxiservice.dto.request.CreateCommentRequest;
import com.dusan.taxiservice.dto.response.CommentResponse;

public interface RideCommentService {

    void createComment(long rideId, String clientUsername, CreateCommentRequest createCommentReqeust);
    CommentResponse findComment(long rideId, String clientUsername);
}
