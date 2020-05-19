package com.dusan.taxiservice.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {

    private long id;
    private LocalDateTime creationDateTime;
    private String clientUsername;
    private long rideId;
    private String comment;
}
