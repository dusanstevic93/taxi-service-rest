package com.dusan.taxiservice.core.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dusan.taxiservice.core.entity.User;
import com.dusan.taxiservice.core.dao.projection.LoginProjection;
import com.dusan.taxiservice.core.dao.projection.UserProjection;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    
    @Query("SELECT new com.dusan.taxiservice.core.dao.projection.UserProjection("
            + "user.username,"
            + "user.firstName,"
            + "user.lastName,"
            + "user.gender,"
            + "user.phone,"
            + "user.email"
            + ")"
            + "FROM User user where user.username = :username")
    Optional<UserProjection> getUserProjection(@Param("username") String username);
    
    @Query("SELECT new com.dusan.taxiservice.core.dao.projection.LoginProjection("
            + "user.username,"
            + "user.password,"
            + "user.role.id"
            + ")"
            + "FROM User user WHERE user.username = :username")
    Optional<LoginProjection> getLoginProjection(@Param("username") String username);
}
