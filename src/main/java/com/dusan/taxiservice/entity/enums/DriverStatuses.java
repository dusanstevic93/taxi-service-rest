package com.dusan.taxiservice.entity.enums;

import java.util.Arrays;

public enum DriverStatuses {

    NOT_WORKING(1), ON_RIDE(2), WAITING_FOR_RIDE(3);
    
    private final long id;
    
    DriverStatuses(long id){
        this.id = id;
    }
    
    public static DriverStatuses fromId(long id) {
        return Arrays.stream(values())
                .filter(status -> status.getId() == id)
                .findFirst().get();
    }
    
    public long getId() {
        return id;
    }
}
