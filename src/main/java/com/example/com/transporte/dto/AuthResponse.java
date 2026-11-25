package com.example.com.transporte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String nombreCompleto;
    private String email;
    private String rol;
}
