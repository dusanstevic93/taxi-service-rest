package com.dusan.taxiservice.core.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageParams {
    
    @Parameter(description = "Current page. Default value is 0. Min value is 0")
    @Min(0)
    private int page = 0;
    
    @Parameter(description = "Number of items per page. Default value is 10. Min value is 1")
    @Min(1)
    @Max(30)
    private int limit = 10;
}
