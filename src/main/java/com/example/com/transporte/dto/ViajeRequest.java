package com.example.com.transporte.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ViajeRequest {
    private String origen;
    private String destino;
    private Integer cantidadPasajeros;
    private String tipoServicio;
    private LocalDateTime fechaHora;
}
