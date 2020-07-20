package com.dusan.taxiservice.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dusan.taxiservice.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    
}
