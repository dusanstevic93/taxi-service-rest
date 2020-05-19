package com.dusan.taxiservice.converter;

import org.springframework.core.convert.converter.Converter;

import com.dusan.taxiservice.dto.response.ReportResponse;
import com.dusan.taxiservice.entity.RideReport;

public class ReportToReportResponse implements Converter<RideReport, ReportResponse> {

    @Override
    public ReportResponse convert(RideReport source) {
        ReportResponse response = new ReportResponse();
        response.setId(source.getId());
        response.setRideId(source.getRide().getId());
        response.setDriverUsername(source.getDriver().getUsername());
        response.setReport(source.getReport());
        return response;
    }
}
