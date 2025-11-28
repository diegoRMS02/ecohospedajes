package com.ecohospedajes.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.HospedajeRepository;
import com.ecohospedajes.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final HospedajeRepository hospedajeRepository;

    public AdminController(UsuarioRepository usuarioRepository, HospedajeRepository hospedajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.hospedajeRepository = hospedajeRepository;
    }

    // 1. Estadísticas Generales
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("usuarios", usuarioRepository.count());
        stats.put("hospedajes", hospedajeRepository.count());
        return ResponseEntity.ok(stats);
    }

    // 2. Listar Todos los Usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // 3. Eliminar Usuario (Cuidado: Esto borra sus hoteles y reservas en cascada si
    // está configurado)
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 4. Eliminar Hospedaje (Por si es falso)
    @DeleteMapping("/hospedajes/{id}")
    public ResponseEntity<?> eliminarHospedaje(@PathVariable Long id) {
        hospedajeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}