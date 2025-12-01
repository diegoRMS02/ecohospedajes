package com.ecohospedajes.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

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

    public List<DatosHospedaje> listarTodos() {
        return hospedajeRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<DatosHospedaje> listarPorDueno(Long duenoId) {
        return hospedajeRepository.findByPropietarioId(duenoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public DatosHospedaje buscarPorId(Long id) {
        Hospedaje h = hospedajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado con ID: " + id));
        return convertirADTO(h);
    }

    public Page<DatosHospedaje> filtrar(String ubicacion, Double minPrice, Double maxPrice, int pagina) {
        if (minPrice == null)
            minPrice = 0.0;
        if (maxPrice == null)
            maxPrice = 10000.0;

        PageRequest pageRequest = PageRequest.of(pagina, 5);

        Page<Hospedaje> resultados = hospedajeRepository.buscarConFiltros(ubicacion, minPrice, maxPrice, pageRequest);

        return resultados.map(this::convertirADTO);
    }


    @Transactional
    public DatosHospedaje guardar(DatosHospedaje datos) {
        Usuario dueno = usuarioRepository.findById(datos.getPropietarioId())
                .orElseThrow(() -> new RuntimeException("El dueño no existe"));

        Hospedaje nuevo = new Hospedaje();
        nuevo.setNombre(datos.getNombre());
        nuevo.setUbicacion(datos.getUbicacion());
        nuevo.setPrecio(datos.getPrecio());
        nuevo.setImagenUrl(datos.getImagenUrl());
        nuevo.setDescripcion(datos.getDescripcion());
        nuevo.setServicios(datos.getServicios());
        nuevo.setPropietario(dueno);

        Hospedaje guardado = hospedajeRepository.save(nuevo);

        datos.setId(guardado.getId());
        datos.setNombrePropietario(dueno.getNombre());
        return datos;
    }

    @Transactional
    public DatosHospedaje actualizarHospedaje(Long id, DatosHospedaje datosNuevos) {
        Hospedaje hospedaje = hospedajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado para editar"));

        hospedaje.setNombre(datosNuevos.getNombre());
        hospedaje.setDescripcion(datosNuevos.getDescripcion());
        hospedaje.setUbicacion(datosNuevos.getUbicacion());
        hospedaje.setPrecio(datosNuevos.getPrecio());
        hospedaje.setImagenUrl(datosNuevos.getImagenUrl());
        hospedaje.setServicios(datosNuevos.getServicios());

        Hospedaje actualizado = hospedajeRepository.save(hospedaje);

        return convertirADTO(actualizado);
    }

    @Transactional
    public void eliminarHospedaje(Long id) {
        hospedajeRepository.deleteById(id);
    }

    private DatosHospedaje convertirADTO(Hospedaje h) {
        DatosHospedaje dto = new DatosHospedaje();
        dto.setId(h.getId());
        dto.setNombre(h.getNombre());
        dto.setUbicacion(h.getUbicacion());
        dto.setPrecio(h.getPrecio());
        dto.setImagenUrl(h.getImagenUrl());
        dto.setDescripcion(h.getDescripcion());
        dto.setServicios(h.getServicios());

        if (h.getPropietario() != null) {
            dto.setPropietarioId(h.getPropietario().getId());
            dto.setNombrePropietario(h.getPropietario().getNombre());
        }
        return dto;
    }
}