package com.ecohospedajes.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecohospedajes.api.dto.DatosActualizacionUsuario;
import com.ecohospedajes.api.dto.DatosRegistro;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; 

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(DatosRegistro datos) {
        if (usuarioRepository.existsByEmail(datos.getEmail())) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(datos.getNombre());
        nuevo.setApellidos(datos.getApellidos());
        nuevo.setEmail(datos.getEmail());
        nuevo.setRol(datos.getRol());
        nuevo.setPassword(passwordEncoder.encode(datos.getPassword()));

        return usuarioRepository.save(nuevo);
    }

    public Usuario actualizar(Long id, DatosActualizacionUsuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(datos.getNombre());
        usuario.setApellidos(datos.getApellidos());

        if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(datos.getPassword())); // Encriptar si cambia
        }

        return usuarioRepository.save(usuario);
    }
}