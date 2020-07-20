package com.dusan.taxiservice.dao.projection;

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
