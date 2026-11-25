package com.example.com.transporte.config;

import com.example.com.transporte.model.Usuario;
import com.example.com.transporte.model.Vehiculo;
import com.example.com.transporte.model.Viaje;
import com.example.com.transporte.repository.UsuarioRepository;
import com.example.com.transporte.repository.VehiculoRepository;
import com.example.com.transporte.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ViajeRepository viajeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            // 1. Usuarios de prueba (todos con contraseña: 123456)
            Usuario cliente = usuarioRepository.save(new Usuario(null, "Colegio San Andrés", "mosquito@gmail.com", passwordEncoder.encode("123456"), "CLIENTE"));
            Usuario conductor = usuarioRepository.save(new Usuario(null, "Carlos Pérez", "conductor@gmail.com", passwordEncoder.encode("123456"), "CONDUCTOR"));
            usuarioRepository.save(new Usuario(null, "Administrador Flota", "admin@gmail.com", passwordEncoder.encode("123456"), "ADMIN"));

            // 2. Vehículos de prueba
            Vehiculo bus1 = vehiculoRepository.save(new Vehiculo(null, "AB1234", "Mercedes BUS 400", 50, "DISPONIBLE"));
            vehiculoRepository.save(new Vehiculo(null, "CD5678", "Volvo Paradiso 1800", 45, "DISPONIBLE"));
            vehiculoRepository.save(new Vehiculo(null, "EF9012", "Scania Marcopolo", 40, "FUERA_DE_SERVICIO"));

            // 3. Un viaje ya asignado al conductor (para ver en su pantalla) y uno pendiente (para asignar en Admin)
            viajeRepository.save(new Viaje(null, "Las Condes", "Farellones", 35, "Transporte escolar (SR001)",
                    LocalDateTime.now().plusDays(1), "ASIGNADO", null, cliente, conductor, bus1));

            viajeRepository.save(new Viaje(null, "Providencia", "Aeropuerto SCL", 20, "Traslado ejecutivo (SR002)",
                    LocalDateTime.now().plusDays(2), "PENDIENTE", null, cliente, null, null));
        }
    }
}
