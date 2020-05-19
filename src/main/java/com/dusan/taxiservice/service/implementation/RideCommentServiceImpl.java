package com.dusan.taxiservice.service.implementation;

import java.time.LocalDateTime;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.dao.ClientRepository;
import com.dusan.taxiservice.dao.RideCommentRepository;
import com.dusan.taxiservice.dao.RideRepository;
import com.dusan.taxiservice.dto.request.CreateCommentRequest;
import com.dusan.taxiservice.dto.response.CommentResponse;
import com.dusan.taxiservice.entity.Client;
import com.dusan.taxiservice.entity.Ride;
import com.dusan.taxiservice.entity.RideComment;
import com.dusan.taxiservice.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.RideCommentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideCommentServiceImpl implements RideCommentService {

    private ClientRepository clientRepository;
    private RideCommentRepository commentRepository;
    private RideRepository rideRepository;
    private ConversionService conversion;
    
    @Override
    public CommentResponse createComment(long rideId, String clientUsername, CreateCommentRequest createCommentRequest) {
        Ride ride = findRide(rideId, clientUsername);
        RideComment rideComment = new RideComment();
        rideComment.setCreationDateTime(LocalDateTime.now());
        rideComment.setComment(createCommentRequest.getText());
        rideComment.setClient(clientRepository.getOne(clientUsername));
        rideComment.setRide(ride);
        RideComment savedComment = commentRepository.save(rideComment);
        return conversion.convert(savedComment, CommentResponse.class);
    }
    
    private Ride findRide(long rideId, String clientUsername) {
        Ride ride = rideRepository.findByIdAndClient(rideId, clientRepository.getOne(clientUsername))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return ride;
    }

    @Override
    public CommentResponse findComment(long rideId, String clientUsername) {
        Ride ride = rideRepository.getOne(rideId);
        Client client = clientRepository.getOne(clientUsername);
        RideComment comment = commentRepository.findByRideAndClient(ride, client)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return conversion.convert(comment, CommentResponse.class);
    }  
}
