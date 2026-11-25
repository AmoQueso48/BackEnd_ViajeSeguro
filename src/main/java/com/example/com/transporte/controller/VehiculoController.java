package com.example.com.transporte.controller;

import com.example.com.transporte.model.Vehiculo;
import com.example.com.transporte.repository.VehiculoRepository;
import com.example.com.transporte.repository.ViajeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos y Métricas", description = "CRUD de flota de buses y estadísticas para el dashboard Admin")
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository;
    private final ViajeRepository viajeRepository;

    public VehiculoController(VehiculoRepository vehiculoRepository, ViajeRepository viajeRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.viajeRepository = viajeRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos")
    public ResponseEntity<List<Vehiculo>> listarTodos() {
        return ResponseEntity.ok(vehiculoRepository.findAll());
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar vehículos con estado DISPONIBLE")
    public ResponseEntity<List<Vehiculo>> listarDisponibles() {
        return ResponseEntity.ok(vehiculoRepository.findByEstado("DISPONIBLE"));
    }

    @GetMapping("/metricas")
    @Operation(summary = "Obtener métricas del dashboard del Administrador")
    public ResponseEntity<?> obtenerMetricas() {
        long totalVehiculos = vehiculoRepository.count();
        long enRuta = vehiculoRepository.findByEstado("EN_RUTA").size();
        long fueraServicio = vehiculoRepository.findByEstado("FUERA_DE_SERVICIO").size();
        long totalViajes = viajeRepository.count();
        long viajesRetraso = viajeRepository.findByEstado("CON_RETRASO").size();

        int utilizacionFlota = totalVehiculos > 0 ? (int) ((enRuta * 100) / totalVehiculos) : 0;
        int pctFueraServicio = totalVehiculos > 0 ? (int) ((fueraServicio * 100) / totalVehiculos) : 0;
        int pctRetraso = totalViajes > 0 ? (int) ((viajesRetraso * 100) / totalViajes) : 0;

        Map<String, Integer> metricas = new HashMap<>();
        metricas.put("utilizacionFlota", utilizacionFlota);
        metricas.put("viajesConRetraso", pctRetraso);
        metricas.put("aumentoCombustible", 15);
        metricas.put("vehiculosFueraServicio", pctFueraServicio);

        return ResponseEntity.ok(metricas);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo vehículo")
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoRepository.save(vehiculo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un vehículo existente")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @RequestBody Vehiculo datos) {
        Vehiculo v = vehiculoRepository.findById(id).orElseThrow();
        v.setPatente(datos.getPatente());
        v.setModelo(datos.getModelo());
        v.setCapacidad(datos.getCapacidad());
        v.setEstado(datos.getEstado());
        return ResponseEntity.ok(vehiculoRepository.save(v));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un vehículo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}