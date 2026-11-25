package com.example.com.transporte.dto;

import lombok.Data;

@Data
public class NovedadRequest {
    private Long viajeId;
    private String tipoNovedad;
    private String descripcion;
}
