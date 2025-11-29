package com.ecohospedajes.api.service;

import org.springframework.stereotype.Service;

import com.ecohospedajes.api.dto.DatosActualizacionUsuario;
import com.ecohospedajes.api.dto.DatosLogin;
import com.ecohospedajes.api.dto.DatosRegistro;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.UsuarioRepository; // <--- NUEVO IMPORT

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // --- MÉTODOS DE AUTENTICACIÓN ---

    public Usuario registrar(DatosRegistro datos) {
        if (usuarioRepository.existsByEmail(datos.getEmail())) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(datos.getNombre());
        nuevo.setApellidos(datos.getApellidos());
        nuevo.setEmail(datos.getEmail());
        nuevo.setRol(datos.getRol());
        nuevo.setPassword(datos.getPassword());

        return usuarioRepository.save(nuevo);
    }

    public Usuario login(DatosLogin datos) {
        Usuario usuario = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(datos.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }

    // --- MÉTODOS DE GESTIÓN (CRUD) ---

    // Método de Actualización
    public Usuario actualizar(Long id, DatosActualizacionUsuario datos) { // <--- CAMBIO DE DTO
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para actualizar"));

        // 1. Actualizamos campos
        usuario.setNombre(datos.getNombre());
        usuario.setApellidos(datos.getApellidos());

        // 2. Si manda contraseña, la actualizamos (solo si no es nula NI vacía)
        if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
            usuario.setPassword(datos.getPassword());
        }

        return usuarioRepository.save(usuario);
    }
}