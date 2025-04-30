package com.wcc.postcode.jwt;

import lombok.Getter;
import lombok.Setter;

class AuthenticationResponse {
    @Setter
    @Getter
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }
}