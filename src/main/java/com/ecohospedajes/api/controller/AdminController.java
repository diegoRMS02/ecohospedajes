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
import com.ecohospedajes.api.repository.ReservaRepository; // ← IMPORTANTE

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final HospedajeRepository hospedajeRepository;
    private final ReservaRepository reservaRepository; // ← NUEVO

    public AdminController(
            UsuarioRepository usuarioRepository,
            HospedajeRepository hospedajeRepository,
            ReservaRepository reservaRepository // ← NUEVO
    ) {
        this.usuarioRepository = usuarioRepository;
        this.hospedajeRepository = hospedajeRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("usuarios", usuarioRepository.count());
        stats.put("hospedajes", hospedajeRepository.count());
        stats.put("reservas", reservaRepository.count()); // ← NUEVO
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
      
    @DeleteMapping("/hospedajes/{id}")
    public ResponseEntity<?> eliminarHospedaje(@PathVariable Long id) {
        hospedajeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
