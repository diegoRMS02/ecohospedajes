package com.ecohospedajes.api.dto;

public class DatosJWTToken {
    private String token;

    public DatosJWTToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}