package com.example.com.transporte.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String rol; // "CLIENTE", "CONDUCTOR", "ADMIN"
}
