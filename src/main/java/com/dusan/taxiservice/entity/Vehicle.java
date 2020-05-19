package com.dusan.taxiservice.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private int productionYear;
    
    private String licencePlate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;
    
    @OneToMany(mappedBy = "vehicle")
    private List<Driver> drivers; 
}
