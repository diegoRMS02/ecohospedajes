package com.ecohospedajes.api.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.dto.DatosRegistro;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.service.UsuarioService;
import com.ecohospedajes.api.dto.DatosActualizacionUsuario; // <--- NUEVO IMPORT
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. POST /api/usuarios/registro (Crear Usuario)
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody DatosRegistro request) {
        Usuario nuevoUsuario = usuarioService.registrar(request);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // 2. POST /api/usuarios/login (Login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody com.ecohospedajes.api.dto.DatosLogin datos) {
        Usuario usuario = usuarioService.login(datos);
        return ResponseEntity.ok(usuario);
    }

    // 3. PUT /api/usuarios/{id} (ACTUALIZAR PERFIL)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id,
            @Valid @RequestBody DatosActualizacionUsuario datosNuevos) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizar(id, datosNuevos);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }
}