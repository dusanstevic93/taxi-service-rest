package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private long id;
    private LocalDateTime creationDateTime;
    private String clientUsername;
    private long rideId;
    private String comment;
}
