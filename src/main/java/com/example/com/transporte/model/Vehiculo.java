package com.example.com.transporte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String patente; // Ej: "AB1234"

    @Column(nullable = false)
    private String modelo; // Ej: "Mercedes BUS 400"

    @Column(nullable = false)
    private Integer capacidad; // Ej: 50

    @Column(nullable = false)
    private String estado; // "DISPONIBLE", "EN_RUTA", "FUERA_DE_SERVICIO"
}
