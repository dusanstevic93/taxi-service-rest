package com.dusan.taxiservice.entity.enums;

import java.util.Arrays;


public enum RideStatuses {

    CREATED(1), CANCELED(2), FORMED(3), PROCESSED(4),
    ACCEPTED(5), FAILED(6), SUCCESSFUL(7);
    
    private final long id;
    
    public static RideStatuses fromId(long id) {
        return Arrays.stream(values())
                .filter(status -> status.getId() == id)
                .findFirst().get();
    }
    
    private RideStatuses(long id){
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
}
