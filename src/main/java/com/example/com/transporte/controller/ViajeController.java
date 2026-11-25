package com.example.com.transporte.controller;

import com.example.com.transporte.dto.AsignacionRequest;
import com.example.com.transporte.dto.ViajeRequest;
import com.example.com.transporte.model.Viaje;
import com.example.com.transporte.service.ViajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@Tag(name = "Viajes", description = "Operaciones CRUD de solicitud, asignación, estado y calificación de viajes")
public class ViajeController {

    private final ViajeService viajeService;

    @GetMapping
    @Operation(summary = "Obtener todos los viajes")
    public ResponseEntity<List<Viaje>> listarTodos() {
        return ResponseEntity.ok(viajeService.listarTodos());
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Obtener viajes pendientes de asignación (Admin)")
    public ResponseEntity<List<Viaje>> listarPendientes() {
        return ResponseEntity.ok(viajeService.listarPendientes());
    }

    @GetMapping("/mis-viajes")
    @Operation(summary = "Obtener viajes del cliente o conductor autenticado")
    public ResponseEntity<List<Viaje>> listarMisViajes(Authentication authentication) {
        return ResponseEntity.ok(viajeService.listarPorUsuarioAutenticado(authentication.getName()));
    }

    @PostMapping
    @Operation(summary = "Solicitar un nuevo viaje (Cliente)")
    public ResponseEntity<Viaje> crearViaje(@RequestBody ViajeRequest request, Authentication authentication) {
        Viaje creado = viajeService.crearViaje(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}/asignar")
    @Operation(summary = "Asignar vehículo y conductor a un viaje (Admin)")
    public ResponseEntity<Viaje> asignar(@PathVariable Long id, @RequestBody AsignacionRequest request) {
        return ResponseEntity.ok(viajeService.asignarVehiculoYConductor(id, request));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del viaje (EN_CURSO, FINALIZADO, CON_RETRASO)")
    public ResponseEntity<Viaje> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(viajeService.actualizarEstado(id, body.get("estado")));
    }

    @PutMapping("/{id}/calificar")
    @Operation(summary = "Calificar un viaje finalizado (Cliente)")
    public ResponseEntity<Viaje> calificar(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(viajeService.calificarViaje(id, body.get("calificacion")));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar o cancelar un viaje")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajeService.eliminarViaje(id);
        return ResponseEntity.noContent().build();
    }
}
