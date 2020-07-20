package com.dusan.taxiservice.dao.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.enums.RideStatuses;

public class GenericRideSpecification implements Specification<Ride> {

    private static final long serialVersionUID = -2537507271917920725L;
    
    private RideStatuses rideStatus;
    private LocalDateTime dateFrom;  
    private LocalDateTime dateTo;  
    private Integer ratingFrom; 
    private Integer ratingTo;  
    private Integer priceFrom;   
    private Integer priceTo;
    
    public GenericRideSpecification(RideQueryParams params) {
        rideStatus = params.getRideStatus();
        dateFrom = params.getDateFrom();
        dateTo = params.getDateTo();
        ratingFrom = params.getRatingFrom();
        ratingTo = params.getRatingTo();
        priceFrom = params.getPriceFrom();
        priceTo = params.getPriceTo();
        
    }
    
    @Override
    public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (rideStatus != null) {
            Predicate p = builder.equal(root.get("rideStatus").get("id"), rideStatus.getId());
            predicates.add(p);
        }
        
        if (dateFrom != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("creationDateTime"), dateFrom);
            predicates.add(p);
        }
        
        if (dateTo != null) {
            Predicate p = builder.lessThanOrEqualTo(root.get("creationDateTime"), dateTo);
            predicates.add(p);
        }
        
        if (ratingFrom != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("rating"), ratingFrom);
            predicates.add(p);
        }
        
        if (ratingTo != null) {
            Predicate p = builder.lessThanOrEqualTo(root.get("rating"), ratingTo);
            predicates.add(p);
        }
        
        if (priceFrom != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("value"), priceFrom);
            predicates.add(p);
        }
        
        if (priceTo != null) {
            Predicate p = builder.lessThanOrEqualTo(root.get("value"), priceTo);
            predicates.add(p);
        }
        
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
