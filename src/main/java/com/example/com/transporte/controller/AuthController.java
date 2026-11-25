package com.example.com.transporte.controller;

import com.example.com.transporte.dto.AuthResponse;
import com.example.com.transporte.dto.LoginRequest;
import com.example.com.transporte.dto.RegistroRequest;
import com.example.com.transporte.model.Usuario;
import com.example.com.transporte.repository.UsuarioRepository;
import com.example.com.transporte.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación y Usuarios", description = "Registro, login con JWT y consulta de conductores")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El email ya está registrado"));
        }

        String rolAsignado = (request.getRol() == null || request.getRol().isBlank())
                ? "CLIENTE" : request.getRol().toUpperCase();

        Usuario nuevo = new Usuario();
        nuevo.setNombreCompleto(request.getNombreCompleto());
        nuevo.setEmail(request.getEmail());
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevo.setRol(rolAsignado);

        Usuario guardado = usuarioRepository.save(nuevo);
        String token = jwtService.generarToken(guardado.getEmail(), guardado.getRol());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, guardado.getId(), guardado.getNombreCompleto(), guardado.getEmail(), guardado.getRol()));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Credenciales incorrectas"));
        }

        if (request.getRol() != null && !request.getRol().isBlank()
                && !usuario.getRol().equalsIgnoreCase(request.getRol())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "El rol seleccionado no coincide con el usuario"));
        }

        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol());
        return ResponseEntity.ok(new AuthResponse(token, usuario.getId(), usuario.getNombreCompleto(), usuario.getEmail(), usuario.getRol()));
    }

    @GetMapping("/conductores")
    @Operation(summary = "Listar usuarios con rol CONDUCTOR (para selector Admin)")
    public ResponseEntity<List<Usuario>> listarConductores() {
        return ResponseEntity.ok(usuarioRepository.findByRol("CONDUCTOR"));
    }
}
