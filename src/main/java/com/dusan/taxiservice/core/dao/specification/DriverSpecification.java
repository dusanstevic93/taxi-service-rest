package com.dusan.taxiservice.core.dao.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dusan.taxiservice.core.service.model.DriverQueryParams;
import com.dusan.taxiservice.core.entity.Driver;
import com.dusan.taxiservice.core.entity.enums.DriverStatuses;
import com.dusan.taxiservice.core.entity.enums.VehicleTypes;

public class DriverSpecification implements Specification<Driver> {

    private static final long serialVersionUID = -1934283240287864244L;

    private DriverStatuses status;
    private VehicleTypes vehicleType;
    
    public DriverSpecification(DriverQueryParams params) {
        status = params.getDriverStatus();
        vehicleType = params.getVehicleType();
    }
    
    @Override
    public Predicate toPredicate(Root<Driver> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (status != null) {
            Predicate p = builder.equal(root.get("status").get("id"), status.getId());
            predicates.add(p);
        }
        
        if (vehicleType != null) {
            Predicate p = builder.equal(root.get("vehicle").get("vehicleType").get("id"), vehicleType.getId());
            predicates.add(p);
        }
        
        root.fetch("vehicle");
        
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
