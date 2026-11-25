package com.example.com.transporte.service;

import com.example.com.transporte.dto.AsignacionRequest;
import com.example.com.transporte.dto.ViajeRequest;
import com.example.com.transporte.model.Usuario;
import com.example.com.transporte.model.Vehiculo;
import com.example.com.transporte.model.Viaje;
import com.example.com.transporte.repository.UsuarioRepository;
import com.example.com.transporte.repository.VehiculoRepository;
import com.example.com.transporte.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;

    public List<Viaje> listarTodos() {
        return viajeRepository.findAll();
    }

    public List<Viaje> listarPendientes() {
        return viajeRepository.findByEstado("PENDIENTE");
    }

    public List<Viaje> listarPorUsuarioAutenticado(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if ("CONDUCTOR".equalsIgnoreCase(usuario.getRol())) {
            return viajeRepository.findByConductorId(usuario.getId());
        } else if ("CLIENTE".equalsIgnoreCase(usuario.getRol())) {
            return viajeRepository.findByClienteId(usuario.getId());
        }
        return viajeRepository.findAll();
    }

    public Viaje crearViaje(ViajeRequest request, String emailCliente) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Viaje viaje = new Viaje();
        viaje.setOrigen(request.getOrigen());
        viaje.setDestino(request.getDestino());
        viaje.setCantidadPasajeros(request.getCantidadPasajeros());
        viaje.setTipoServicio(request.getTipoServicio());
        viaje.setFechaHora(request.getFechaHora());
        viaje.setEstado("PENDIENTE");
        viaje.setCliente(cliente);

        return viajeRepository.save(viaje);
    }

    public Viaje asignarVehiculoYConductor(Long viajeId, AsignacionRequest request) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Usuario conductor = usuarioRepository.findById(request.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        viaje.setVehiculo(vehiculo);
        viaje.setConductor(conductor);
        viaje.setEstado("ASIGNADO");

        vehiculo.setEstado("EN_RUTA");
        vehiculoRepository.save(vehiculo);

        return viajeRepository.save(viaje);
    }

    public Viaje actualizarEstado(Long viajeId, String nuevoEstado) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        viaje.setEstado(nuevoEstado.toUpperCase());
        return viajeRepository.save(viaje);
    }

    public Viaje calificarViaje(Long viajeId, Integer calificacion) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        viaje.setCalificacion(calificacion);
        viaje.setEstado("FINALIZADO");
        return viajeRepository.save(viaje);
    }

    public void eliminarViaje(Long viajeId) {
        if (!viajeRepository.existsById(viajeId)) {
            throw new RuntimeException("Viaje no encontrado");
        }
        viajeRepository.deleteById(viajeId);
    }
}
