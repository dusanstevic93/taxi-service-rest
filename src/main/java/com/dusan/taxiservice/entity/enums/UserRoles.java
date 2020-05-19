package com.dusan.taxiservice.entity.enums;

import java.util.Arrays;

public enum UserRoles {
    
    CLIENT(1), DRIVER(2), DISPATCHER(3);
    
    private final long id;
    
    private UserRoles(long id){
        this.id = id;
    }
    
    public static UserRoles fromId(long id) {
        return Arrays.stream(values())
                .filter(role -> role.id == id)
                .findFirst()
                .get();
    }
    
    public long getId() {
        return id;
    }

}
