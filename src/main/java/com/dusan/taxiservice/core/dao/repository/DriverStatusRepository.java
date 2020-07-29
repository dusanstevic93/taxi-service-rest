package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.DriverStatus;

public interface DriverStatusRepository extends JpaRepository<DriverStatus, Long> {

}
