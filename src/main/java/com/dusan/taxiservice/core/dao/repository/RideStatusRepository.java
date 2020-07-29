package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.RideStatus;

public interface RideStatusRepository extends JpaRepository<RideStatus, Long> {

}
