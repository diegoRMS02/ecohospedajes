package com.ecohospedajes.api.service;

import org.springframework.stereotype.Service; // <--- Cambio aquí

import com.ecohospedajes.api.dto.DatosRegistro;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(DatosRegistro datos) { // <--- Cambio aquí
        if (usuarioRepository.existsByEmail(datos.getEmail())) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(datos.getNombre());
        nuevo.setApellidos(datos.getApellidos());
        nuevo.setEmail(datos.getEmail());
        nuevo.setRol(datos.getRol());
        nuevo.setPassword(datos.getPassword()); // Aquí luego pondremos encriptación

        return usuarioRepository.save(nuevo);
    }
}