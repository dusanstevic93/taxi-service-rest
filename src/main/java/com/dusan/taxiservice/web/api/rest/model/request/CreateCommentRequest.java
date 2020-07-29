package com.dusan.taxiservice.web.api.rest.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateCommentRequest {

    @NotBlank
    private String comment;
}
