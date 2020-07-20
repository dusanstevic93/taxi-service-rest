package com.dusan.taxiservice.dao.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.entity.Ride;

public class DispatcherRideSpecification extends GenericRideSpecification {

    private static final long serialVersionUID = -2593350573453578314L;
    
    private String driverFirstName;
    private String driverLastName;
    private String clientFirstName;
    private String clientLastName;
    
    public DispatcherRideSpecification(RideQueryParams params) {
        super(params); 
        driverFirstName = params.getDriverFirstName();
        driverLastName = params.getDriverLastName();
        clientFirstName = params.getClientFirstName();
        clientLastName = params.getClientLastName();
    }

    @Override
    public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(super.toPredicate(root, query, builder));
        
        if (driverFirstName != null) {
            Predicate p = builder.equal(root.join("driver").get("firstName"), driverFirstName);
            predicates.add(p);
        }
        
        if (driverLastName != null) {
            Predicate p = builder.equal(root.join("driver").get("lastName"), driverLastName);
            predicates.add(p);
        }
        
        if (clientFirstName != null) {
            Predicate p = builder.equal(root.join("client").get("firstName"), clientFirstName);
            predicates.add(p);
        }
        
        if (clientLastName != null) {
            Predicate p = builder.equal(root.join("client").get("lastName"), clientLastName);
            predicates.add(p);
        }
        
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));          
    }
}
