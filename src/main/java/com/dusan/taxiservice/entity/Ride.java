package com.dusan.taxiservice.entity;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SelectBeforeUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SelectBeforeUpdate(false)
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private LocalDateTime creationDateTime;
    
    private Integer value;
    
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "starting_location_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "starting_location_longitude"))
    })
    private Location startingLocation;
    
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude"))
    })
    private Location destination;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Dispatcher dispatcher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private RideStatus rideStatus;
    
    private int rating;
}
