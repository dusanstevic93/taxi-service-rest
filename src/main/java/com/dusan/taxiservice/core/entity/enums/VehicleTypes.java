package com.dusan.taxiservice.core.entity.enums;

import java.util.Arrays;

public enum VehicleTypes {

    CAR(1), VAN(2);
    
    private final long id;
    
    private VehicleTypes(long id){
        this.id = id;
    }
    
    public static VehicleTypes fromId(long id) {
        return Arrays.stream(values())
                .filter(type -> type.getId() == id)
                .findFirst().get();
    }
    
    public long getId() {
        return id;
    }
}
