package com.example.com.transporte.controller;

import com.example.com.transporte.dto.NovedadRequest;
import com.example.com.transporte.model.Novedad;
import com.example.com.transporte.model.Viaje;
import com.example.com.transporte.repository.NovedadRepository;
import com.example.com.transporte.repository.ViajeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/novedades")
@RequiredArgsConstructor
@Tag(name = "Novedades", description = "Reporte de incidencias por parte del conductor")
public class NovedadController {

    private final NovedadRepository novedadRepository;
    private final ViajeRepository viajeRepository;

    @GetMapping
    @Operation(summary = "Listar todas las novedades reportadas")
    public ResponseEntity<List<Novedad>> listarTodas() {
        return ResponseEntity.ok(novedadRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Reportar una novedad en un viaje (Conductor)")
    public ResponseEntity<Novedad> crear(@RequestBody NovedadRequest request) {
        Viaje viaje = viajeRepository.findById(request.getViajeId())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        Novedad novedad = new Novedad();
        novedad.setTipoNovedad(request.getTipoNovedad());
        novedad.setDescripcion(request.getDescripcion());
        novedad.setFechaReporte(LocalDateTime.now());
        novedad.setViaje(viaje);

        return ResponseEntity.status(HttpStatus.CREATED).body(novedadRepository.save(novedad));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una novedad")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        novedadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
