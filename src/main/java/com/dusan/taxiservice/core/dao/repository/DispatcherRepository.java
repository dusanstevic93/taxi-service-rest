package com.dusan.taxiservice.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.Dispatcher;

public interface DispatcherRepository extends JpaRepository<Dispatcher, String> {

}
