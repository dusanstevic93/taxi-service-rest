package com.dusan.taxiservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideReport;

public interface RideReportRepository extends JpaRepository<RideReport, Long> {

    Optional<RideReport> findByRideAndDriver(Ride ride, Driver driver);
    Optional<RideReport> findByRide(Ride ride);
}
