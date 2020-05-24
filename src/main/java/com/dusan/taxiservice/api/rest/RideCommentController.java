package com.dusan.taxiservice.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.OpenApiConfig;
import com.dusan.taxiservice.dto.request.CreateCommentRequest;
import com.dusan.taxiservice.dto.response.CommentResponse;
import com.dusan.taxiservice.service.RideCommentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Comment")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@AllArgsConstructor
public class RideCommentController {

    private RideCommentService commentService;
    
    @PostMapping(value = Mappings.RIDE_COMMENT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(
            @PathVariable long rideId, 
            @RequestBody CreateCommentRequest createCommentRequest,
            Authentication auth) {
        commentService.createComment(rideId, auth.getName(), createCommentRequest);
    }
    
    @GetMapping(value = Mappings.RIDE_COMMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentResponse getComment(
            @PathVariable long rideId,
            Authentication auth) {
        return commentService.findComment(rideId, auth.getName());
    }
}
