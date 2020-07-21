package com.dusan.taxiservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}