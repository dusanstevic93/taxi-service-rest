package com.dusan.taxiservice.core.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.core.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    
}
