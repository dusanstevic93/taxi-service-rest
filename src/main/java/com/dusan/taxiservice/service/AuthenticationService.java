package com.dusan.taxiservice.service;

import com.dusan.taxiservice.dto.request.LoginRequest;

public interface AuthenticationService {

    String authenticate(LoginRequest loginRequest);
}
