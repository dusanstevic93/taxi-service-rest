package com.dusan.taxiservice.web.api.rest.controller;

import com.dusan.taxiservice.core.service.model.CommentDto;
import com.dusan.taxiservice.core.service.model.CreateCommentCommand;
import com.dusan.taxiservice.web.api.rest.model.request.CreateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.web.api.rest.docs.OpenApiConfig;
import com.dusan.taxiservice.core.service.RideCommentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

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
            @Valid @RequestBody CreateCommentRequest createCommentRequest,
            Authentication auth) {
        CreateCommentCommand createCommand = new CreateCommentCommand();
        createCommand.setRideId(rideId);
        createCommand.setClientUsername(auth.getName());
        createCommand.setComment(createCommentRequest.getComment());
        commentService.createComment(createCommand);
    }
    
    @GetMapping(value = Mappings.RIDE_COMMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDto getComment(@PathVariable long rideId, Authentication auth) {
        return commentService.findComment(rideId, auth.getName());
    }
}
