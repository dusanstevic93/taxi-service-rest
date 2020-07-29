package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateCommentCommand {

    private long rideId;

    @NotBlank
    private String clientUsername;

    @NotBlank
    private String comment;
}
