package com.example.com.transporte.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombreCompleto;
    private String email;
    private String password;
    private String rol; // Por defecto será "CLIENTE" si viene vacío
}
