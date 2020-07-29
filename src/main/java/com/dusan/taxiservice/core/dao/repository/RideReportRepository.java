package com.dusan.taxiservice.core.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.RideReport;

public interface RideReportRepository extends JpaRepository<RideReport, Long> {

    Optional<RideReport> findByRideAndDriver(Ride ride, Driver driver);
    Optional<RideReport> findByRide(Ride ride);
}
