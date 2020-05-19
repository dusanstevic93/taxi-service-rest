package com.dusan.taxiservice.entity.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginProjection {

    private String username;
    private String password;
    private long roleId;
}
