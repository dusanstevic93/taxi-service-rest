package com.dusan.taxiservice.model;

import com.dusan.taxiservice.dto.LocationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonModels {

    public static LocationDto getLocationDtoModel() {
        LocationDto location = new LocationDto(25.85, 13.26);
        return location;
    }
}
