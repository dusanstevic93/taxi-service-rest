package com.dusan.taxiservice.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.projection.UserProjection;

public interface DriverRepository extends JpaRepository<Driver, String>, JpaSpecificationExecutor<Driver> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Driver> findByUsernameAndVehicleVehicleTypeId(String driverUsername, long vehicleTypeId);
    
    @Query("SELECT d FROM Driver d JOIN FETCH d.vehicle v WHERE d.username = :username")
    Optional<Driver> findByUsernameFetchVehicle(@Param("username") String username);
    
    @Modifying
    @Query(value = "UPDATE driver SET status_id = :status WHERE username = :username", nativeQuery = true)
    void updateStatus(@Param("username") String username, @Param("status") long statusId);
    
    @Query(value = "SELECT driver.status.id FROM Driver driver WHERE driver.username = :username")
    long getCurrentStatusId(@Param("username") String username);
    
    @Query("SELECT new com.dusan.taxiservice.entity.projection.UserProjection("
            + "driver.username,"
            + "driver.firstName,"
            + "driver.lastName,"
            + "driver.gender,"
            + "driver.phone,"
            + "driver.email"
            + ")"
            + "FROM Driver driver WHERE driver.vehicle.id = :vehicleId")
    List<UserProjection> findAllByVehicleId(@Param("vehicleId") long vehicleId);
}
