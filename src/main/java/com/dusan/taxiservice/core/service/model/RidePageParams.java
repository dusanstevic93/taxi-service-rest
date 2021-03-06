package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RidePageParams extends PageParams {
  
    private PageSort sort = PageSort.UNSORTED;
    
    public enum PageSort {
        DATE, RATING, UNSORTED
    }
}
