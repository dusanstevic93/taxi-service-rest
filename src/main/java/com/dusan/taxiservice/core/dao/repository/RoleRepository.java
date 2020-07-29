package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.UserRole;

public interface RoleRepository extends JpaRepository<UserRole, Long> {

}
