package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
