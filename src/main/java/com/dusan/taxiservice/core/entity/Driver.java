package com.dusan.taxiservice.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Driver extends User {

    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "current_location_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "current_location_longitude"))
    })
    private Location currentLocation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private DriverStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
    
    public Driver(boolean isNew) {
        super(isNew);
    }
}
