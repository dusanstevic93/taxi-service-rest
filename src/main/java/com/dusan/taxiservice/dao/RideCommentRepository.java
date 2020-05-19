package com.dusan.taxiservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideComment;

public interface RideCommentRepository extends JpaRepository<RideComment, Long> {

    Optional<RideComment> findByRideAndClient(Ride ride, Client client);
}
