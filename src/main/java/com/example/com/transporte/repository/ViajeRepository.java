package com.example.com.transporte.repository;

import com.example.com.transporte.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEstado(String estado);
    List<Viaje> findByClienteId(Long clienteId);
    List<Viaje> findByConductorId(Long conductorId);
}
