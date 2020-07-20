package com.dusan.taxiservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.Dispatcher;

public interface DispatcherRepository extends JpaRepository<Dispatcher, String> {

}
