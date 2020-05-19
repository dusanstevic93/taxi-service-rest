package com.dusan.taxiservice.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dto.request.RidePageParams;
import com.dusan.taxiservice.dto.request.RidePageParams.PageSort;
import com.dusan.taxiservice.dto.request.RideQueryParams;
import com.dusan.taxiservice.dto.response.RideResponse;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.enums.RideStatuses;
import com.dusan.taxiservice.entity.enums.UserRoles;
import com.dusan.taxiservice.entity.specification.DispatcherRideSpecification;
import com.dusan.taxiservice.entity.specification.GenericRideSpecification;
import com.dusan.taxiservice.entity.specification.RoleRideSpecification;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideFinder {

    private RideRepository rideRepository;
    private ConversionService conversion;

    
    public List<RideResponse> findAllRidesOfSpecificUser(String username, UserRoles userRole, RideQueryParams queryParams,
            RidePageParams pageParams) {
        Specification<Ride> specification = determineSpecification(userRole, queryParams)
                .and(new RoleRideSpecification(username, userRole));
        
        return getResponseList(specification, pageParams);        
    }
    
    private Specification<Ride> determineSpecification(UserRoles userRole, RideQueryParams params) {
        Specification<Ride> specification = null;
        if (userRole == UserRoles.DISPATCHER)
            specification = new DispatcherRideSpecification(params);
        else
            specification = new GenericRideSpecification(params);
        return specification;
    }

    public List<RideResponse> findAllRidesInCreatedStatus(RidePageParams pageParams) {
        Pageable pageable = getPageable(pageParams);
        Page<Ride> page = rideRepository.findAllByStatusId(RideStatuses.CREATED.getId(), pageable);
        return convertPageToResponseList(page);
    }

    public List<RideResponse> findAllRides(RideQueryParams queryParams, RidePageParams pageParams) {
        Specification<Ride> specification = new DispatcherRideSpecification(queryParams);
        return getResponseList(specification, pageParams);
    }
    
    private List<RideResponse> getResponseList(Specification<Ride> specification, RidePageParams pageParams){
        Pageable pageable = getPageable(pageParams);
        Page<Ride> page = rideRepository.findAll(specification, pageable);       
        return convertPageToResponseList(page);
    }

    private Pageable getPageable(RidePageParams pageParams) {
        Sort sort = getSort(pageParams.getSort());
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getLimit(), sort);
        return pageable;
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
    
    private List<RideResponse> convertPageToResponseList(Page<Ride> page){
        List<RideResponse> response = page.getContent()
                .stream()
                .map(ride -> conversion.convert(ride, RideResponse.class))
                .collect(Collectors.toList());
        return response;
    }
}
