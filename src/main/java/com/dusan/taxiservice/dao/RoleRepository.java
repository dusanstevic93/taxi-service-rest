package com.dusan.taxiservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.UserRole;

public interface RoleRepository extends JpaRepository<UserRole, Long> {

}
