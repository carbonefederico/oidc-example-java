package com.pingidentity.oidcapp;

import org.jose4j.jwt.JwtClaims;

public class User {

    private final String accessToken;
    private final String idToken;
    private final JwtClaims idTokenPayload;

    public User(String accessToken,String idToken, JwtClaims idTokenPayload) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.idTokenPayload = idTokenPayload;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getIdToken() { return idToken; }
    public JwtClaims getIdTokenPayload() { return idTokenPayload; }

}
