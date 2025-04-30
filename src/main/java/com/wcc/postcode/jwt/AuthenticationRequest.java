package com.wcc.postcode.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
class AuthenticationRequest {
    private String username;
    private String password;
}