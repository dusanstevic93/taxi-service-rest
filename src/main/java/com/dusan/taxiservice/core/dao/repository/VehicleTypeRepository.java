package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.VehicleType;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

}
