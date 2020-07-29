package com.dusan.taxiservice.core.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.Client;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.RideComment;

public interface RideCommentRepository extends JpaRepository<RideComment, Long> {

    Optional<RideComment> findByRideAndClient(Ride ride, Client client);
}
