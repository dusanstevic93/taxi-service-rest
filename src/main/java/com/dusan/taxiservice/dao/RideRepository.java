package com.dusan.taxiservice.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Driver;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideStatus;

public interface RideRepository extends JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {
    

    Optional<Ride> findByIdAndClient(long id, Client client);
    Optional<Ride> findByIdAndDriver(long id, Driver driver);
    List<Ride> findAllByClientUsername(String clientUsername);
    boolean existsByClientAndRideStatus(Client client, RideStatus status);
    
    @Query(value = "SELECT r FROM Ride r WHERE r.rideStatus.id = :statusId")
    Page<Ride> findAllByStatusId(@Param("statusId") long statusId, Pageable pageable);
}
