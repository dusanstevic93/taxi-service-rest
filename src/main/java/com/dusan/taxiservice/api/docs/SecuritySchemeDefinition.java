package com.dusan.taxiservice.api.docs;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Component
@SecurityScheme(name = SecuritySchemeDefinition.BEARER_TOKEN, type = SecuritySchemeType.HTTP, scheme = "bearer")
public class SecuritySchemeDefinition {
    
    public static final String BEARER_TOKEN = "Bearer token";
}
