package com.dusan.taxiservice.api.rest;

import java.util.Collection;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taxiservice.api.docs.Descriptions;
import com.dusan.taxiservice.api.docs.OpenApiConfig;
import com.dusan.taxiservice.dto.response.ReportResponse;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.service.RideReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Ride report")
@SecurityRequirement(name = OpenApiConfig.BEARER_TOKEN_SCHEME)
@RestController
@AllArgsConstructor
public class RideReportController {

    private RideReportService reportService;
    
    @Operation(summary = "Get ride report", description = Descriptions.GET_RIDE_REPORT,
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation"),
                         @ApiResponse(responseCode = "404", description = "Report is not found")})
    @GetMapping(value = Mappings.RIDE_REPORT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportResponse getReport(
            @PathVariable long rideId, 
            Authentication auth) {
        UserRoles role = getRole(auth.getAuthorities());
        if (role == UserRoles.DISPATCHER)
            return reportService.getAnyReport(rideId);
        else
            return reportService.getSpecificDriverReport(rideId, auth.getName());
    }
    
    private UserRoles getRole(Collection<? extends GrantedAuthority> authorities) {
        String roleWithPrefix = authorities.stream()
                                .map(element -> element.getAuthority())
                                .findFirst()
                                .get();
        return UserRoles.valueOf(roleWithPrefix.replace("ROLE_", ""));   
    }
}
