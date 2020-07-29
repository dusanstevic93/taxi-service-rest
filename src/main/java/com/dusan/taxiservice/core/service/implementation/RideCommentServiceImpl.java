package com.dusan.taxiservice.core.service.implementation;

import java.time.LocalDateTime;

import com.dusan.taxiservice.core.service.model.CommentDto;
import com.dusan.taxiservice.core.service.model.CreateCommentCommand;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dusan.taxiservice.core.dao.repository.ClientRepository;
import com.dusan.taxiservice.core.dao.repository.RideCommentRepository;
import com.dusan.taxiservice.core.dao.repository.RideRepository;
import com.dusan.taxiservice.core.entity.Client;
import com.dusan.taxiservice.core.entity.Ride;
import com.dusan.taxiservice.core.entity.RideComment;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.core.service.RideCommentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class RideCommentServiceImpl implements RideCommentService {

    private ClientRepository clientRepository;
    private RideCommentRepository commentRepository;
    private RideRepository rideRepository;
    private ConversionService conversion;
    
    @Override
    public void createComment(CreateCommentCommand createCommand) {
        Ride ride = getRideFromDatabase(createCommand.getRideId(), createCommand.getClientUsername());
        RideComment rideComment = new RideComment();
        rideComment.setCreationDateTime(LocalDateTime.now());
        rideComment.setComment(createCommand.getComment());
        rideComment.setClient(clientRepository.getOne(createCommand.getClientUsername()));
        rideComment.setRide(ride);
        commentRepository.save(rideComment);
    }
    
    private Ride getRideFromDatabase(long rideId, String clientUsername) {
        return rideRepository.findByIdAndClient(rideId, clientRepository.getOne(clientUsername))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }

    @Override
    public CommentDto findComment(long rideId, String clientUsername) {
        Ride ride = rideRepository.getOne(rideId);
        Client client = clientRepository.getOne(clientUsername);
        RideComment comment = commentRepository.findByRideAndClient(ride, client)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return conversion.convert(comment, CommentDto.class);
    }  
}
