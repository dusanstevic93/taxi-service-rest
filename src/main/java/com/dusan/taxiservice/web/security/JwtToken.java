package com.dusan.taxiservice.web.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtToken {

    private String value;
    private long expiration;
}
