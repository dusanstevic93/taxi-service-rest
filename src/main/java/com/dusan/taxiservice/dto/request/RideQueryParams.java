package com.dusan.taxiservice.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dusan.taxiservice.entity.enums.RideStatuses;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideQueryParams {

    private RideStatuses rideStatus;
    
    @Parameter(description = "YYYY-MM-DD")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateFrom;
    
    @Parameter(description = "YYYY-MM-DD")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateTo;
    
    private Integer ratingFrom;
    
    private Integer ratingTo;
    
    private Integer priceFrom;
    
    private Integer priceTo;
    
    
    // only for dispatchers //
    private String driverFirstName;
    
    private String driverLastName;
    
    private String clientFirstName;
    
    private String clientLastName;
    
    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom.atStartOfDay();
    }
    
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo.atStartOfDay();
    }
    
}
