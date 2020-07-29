package com.dusan.taxiservice.core.service.implementation;

import com.dusan.taxiservice.core.service.model.RideDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.service.model.RidePageParams;
import com.dusan.taxiservice.core.service.model.RidePageParams.PageSort;
import com.dusan.taxiservice.core.service.model.RideQueryParams;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.enums.RideStatuses;
import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.core.dao.specification.DispatcherRideSpecification;
import com.dusan.taxiservice.core.dao.specification.GenericRideSpecification;
import com.dusan.taxiservice.core.dao.specification.RoleRideSpecification;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideFinder {

    private RideRepository rideRepository;
    private ConversionService conversion;

    
    public Page<RideDto> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams,
                                                    RidePageParams pageParams) {
        Specification<Ride> specification = determineSpecification(userRole, queryParams)
                .and(new RoleRideSpecification(username, userRole));
        
        return getRideDtoPage(specification, pageParams);
    }
    
    private Specification<Ride> determineSpecification(UserRoles userRole, RideQueryParams params) {
        Specification<Ride> specification;
        if (userRole == UserRoles.DISPATCHER)
            specification = new DispatcherRideSpecification(params);
        else
            specification = new GenericRideSpecification(params);
        return specification;
    }

    public Page<RideDto> findAllRidesInCreatedStatus(RidePageParams pageParams) {
        Pageable pageable = getPageable(pageParams);
        Page<Ride> page = rideRepository.findAllByStatusId(RideStatuses.CREATED.getId(), pageable);
        return page.map(ride -> conversion.convert(ride, RideDto.class));
    }

    public Page<RideDto> findAllRides(RideQueryParams queryParams, RidePageParams pageParams) {
        Specification<Ride> specification = new DispatcherRideSpecification(queryParams);
        return getRideDtoPage(specification, pageParams);
    }
    
    private Page<RideDto> getRideDtoPage(Specification<Ride> specification, RidePageParams pageParams){
        Pageable pageable = getPageable(pageParams);
        Page<Ride> page = rideRepository.findAll(specification, pageable);       
        return page.map(ride -> conversion.convert(ride, RideDto.class));
    }

    private Pageable getPageable(RidePageParams pageParams) {
        Sort sort = getSort(pageParams.getSort());
        return PageRequest.of(pageParams.getPage(), pageParams.getLimit(), sort);
    }
    
    private Sort getSort(PageSort pageSort) {
        Sort sort = null;
        switch(pageSort){
        case DATE:
            sort = Sort.by("creationDateTime").descending();
            break;
        case RATING:
            sort = Sort.by("rating").descending();
            break;
        case UNSORTED:
            sort = Sort.unsorted();
        }
        return sort;
    }
}
