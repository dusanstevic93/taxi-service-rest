package com.dusan.taxiservice.entity;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Client extends User {

    public Client(boolean isNew) {
        super(isNew);
    }
}
