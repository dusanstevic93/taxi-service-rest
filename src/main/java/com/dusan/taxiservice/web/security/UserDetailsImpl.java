package com.dusan.taxiservice.web.security;

import com.dusan.taxiservice.core.dao.projection.LoginProjection;
import com.dusan.taxiservice.core.entity.enums.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final LoginProjection userDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + UserRoles.fromId(userDetails.getRoleId());
        return Stream.of(new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return userDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
