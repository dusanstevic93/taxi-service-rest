package com.dusan.taxiservice.core.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Setter;

import lombok.Getter;

@Entity
@Getter
@Setter
public class RideComment {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private LocalDateTime creationDateTime;
    
    private String comment;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Client client;
}
