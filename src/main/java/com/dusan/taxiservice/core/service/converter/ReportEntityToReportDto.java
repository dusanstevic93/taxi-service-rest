package com.dusan.taxiservice.core.service.converter;

import com.dusan.taxiservice.core.entity.RideReport;
import com.dusan.taxiservice.core.service.model.ReportDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class ReportEntityToReportDto implements Converter<RideReport, ReportDto> {

    @Override
    public ReportDto convert(RideReport entity) {
        ReportDto dto = new ReportDto();
        dto.setId(entity.getId());
        dto.setRideId(entity.getRide().getId());
        dto.setDriverUsername(entity.getDriver().getUsername());
        dto.setReport(entity.getReport());
        return dto;
    }
}
