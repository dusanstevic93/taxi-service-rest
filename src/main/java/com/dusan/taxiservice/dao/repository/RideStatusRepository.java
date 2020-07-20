package com.dusan.taxiservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.RideStatus;

public interface RideStatusRepository extends JpaRepository<RideStatus, Long> {

}
