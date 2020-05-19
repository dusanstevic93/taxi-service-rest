package com.dusan.taxiservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.DriverStatus;

public interface DriverStatusRepository extends JpaRepository<DriverStatus, Long> {

}
