package com.dusan.taxiservice.web.api.rest.controller;

import com.dusan.taxiservice.core.entity.enums.UserRoles;
import com.dusan.taxiservice.web.api.rest.model.response.PageResponseWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ControllerUtils {

    static <T> PageResponseWrapper<T> createPageResponseWrapper(Page<T> page) {
        PageResponseWrapper.PageMetadata metadata = PageResponseWrapper.PageMetadata.of(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return new PageResponseWrapper<>(page.getContent(), metadata);
    }

    static UserRoles getRole(Collection<? extends GrantedAuthority> authorities) {
        String roleWithPrefix = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .get();
        return UserRoles.valueOf(roleWithPrefix.replace("ROLE_", ""));
    }
}
