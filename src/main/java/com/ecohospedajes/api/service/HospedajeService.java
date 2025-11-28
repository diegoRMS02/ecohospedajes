package com.ecohospedajes.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecohospedajes.api.dto.DatosHospedaje;
import com.ecohospedajes.api.entity.Hospedaje;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.HospedajeRepository;
import com.ecohospedajes.api.repository.UsuarioRepository;

@Service
public class HospedajeService {

    private final HospedajeRepository hospedajeRepository;
    private final UsuarioRepository usuarioRepository;

    public HospedajeService(HospedajeRepository hospedajeRepository, UsuarioRepository usuarioRepository) {
        this.hospedajeRepository = hospedajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 1. Guardar un nuevo hospedaje
    public DatosHospedaje guardar(DatosHospedaje datos) {
        // Buscamos al dueño por su ID
        Usuario dueno = usuarioRepository.findById(datos.getPropietarioId())
                .orElseThrow(() -> new RuntimeException("El dueño no existe"));

        Hospedaje nuevo = new Hospedaje();
        nuevo.setNombre(datos.getNombre());
        nuevo.setUbicacion(datos.getUbicacion());
        nuevo.setPrecio(datos.getPrecio());
        nuevo.setImagenUrl(datos.getImagenUrl());
        nuevo.setDescripcion(datos.getDescripcion());
        nuevo.setPropietario(dueno); // Asignamos la relación

        Hospedaje guardado = hospedajeRepository.save(nuevo);

        // Devolvemos el DTO con el ID generado
        datos.setId(guardado.getId());
        datos.setNombrePropietario(dueno.getNombre());
        return datos;
    }

    // 2. Listar todos (Para el catálogo público)
    public List<DatosHospedaje> listarTodos() {
        return hospedajeRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 3. Buscar por ID (Para el detalle)
    public DatosHospedaje buscarPorId(Long id) {
        Hospedaje h = hospedajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));
        return convertirADTO(h);
    }

    // Método auxiliar para no repetir código (Entity -> DTO)
    private DatosHospedaje convertirADTO(Hospedaje h) {
        DatosHospedaje dto = new DatosHospedaje();
        dto.setId(h.getId());
        dto.setNombre(h.getNombre());
        dto.setUbicacion(h.getUbicacion());
        dto.setPrecio(h.getPrecio());
        dto.setImagenUrl(h.getImagenUrl());
        dto.setDescripcion(h.getDescripcion());
        dto.setPropietarioId(h.getPropietario().getId());
        dto.setNombrePropietario(h.getPropietario().getNombre());
        return dto;
    }
}