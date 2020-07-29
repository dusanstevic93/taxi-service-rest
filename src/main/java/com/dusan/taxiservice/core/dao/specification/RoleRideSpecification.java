package com.dusan.taxiservice.core.dao.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.enums.UserRoles;

public class RoleRideSpecification implements Specification<Ride> {

    private static final long serialVersionUID = 6004988651261624937L;

    private String user;
    private UserRoles role;
    
    public RoleRideSpecification(String user, UserRoles role) {
        this.user = user;
        this.role = role;
    }
    
    @Override
    public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        
        Predicate p = null;
        
        switch(role) {
        case CLIENT: p = builder.equal(root.get("client").get("username"), user);
        break;
        case DRIVER: p = builder.equal(root.get("driver").get("username"), user);
        break;
        case DISPATCHER: p = builder.equal(root.get("dispatcher").get("username"), user);
        break;
        }
        
        return p;       
    }
}
