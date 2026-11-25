package com.example.com.transporte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private Integer cantidadPasajeros;

    @Column(nullable = false)
    private String tipoServicio; // Ej: "Transporte escolar (SR001)"

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String estado; // "PENDIENTE", "ASIGNADO", "EN_CURSO", "FINALIZADO", "CON_RETRASO"

    private Integer calificacion; // 1 a 5 (cuando finaliza)

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Usuario conductor; // Nulo mientras este PENDIENTE

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo; // Nulo mientras este PENDIENTE
}
