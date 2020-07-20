package com.dusan.taxiservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.VehicleType;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

}
