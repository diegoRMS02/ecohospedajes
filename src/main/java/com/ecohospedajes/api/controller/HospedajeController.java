package com.ecohospedajes.api.controller;

import com.ecohospedajes.api.dto.DatosHospedaje;
import com.ecohospedajes.api.service.HospedajeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hospedajes")
@CrossOrigin(origins = "*")
public class HospedajeController {

    private final HospedajeService hospedajeService;

    public HospedajeController(HospedajeService hospedajeService) {
        this.hospedajeService = hospedajeService;
    }

    // --- MÉTODOS PÚBLICOS (Catálogo y Búsqueda) ---

    // GET /api/hospedajes (Búsqueda por filtros con paginación)
    @GetMapping
    public ResponseEntity<Page<DatosHospedaje>> listar(
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page) {

        // Retorna el objeto Page completo (que tiene la lista y los metadatos)
        return ResponseEntity.ok(hospedajeService.filtrar(ubicacion, minPrice, maxPrice, page));
    }

    // GET /api/hospedajes/{id} (Detalle de un solo hotel)
    @GetMapping("/{id}")
    public ResponseEntity<DatosHospedaje> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(hospedajeService.buscarPorId(id));
    }

    // --- MÉTODOS DE GESTIÓN DEL DUEÑO (CRUD) ---

    // GET /api/hospedajes/dueno/{duenoId} (Nuevo: Para llenar la tabla de gestión)
    @GetMapping("/dueno/{duenoId}")
    public ResponseEntity<List<DatosHospedaje>> listarPorDueno(@PathVariable Long duenoId) {
        return ResponseEntity.ok(hospedajeService.listarPorDueno(duenoId));
    }

    // POST /api/hospedajes (Crear nuevo)
    @PostMapping
    public ResponseEntity<DatosHospedaje> crear(@Valid @RequestBody DatosHospedaje datos) {
        return ResponseEntity.ok(hospedajeService.guardar(datos));
    }

    // PUT /api/hospedajes/{id} (Actualizar existente)
    @PutMapping("/{id}")
    public ResponseEntity<DatosHospedaje> actualizar(@PathVariable Long id,
            @Valid @RequestBody DatosHospedaje datosNuevos) {
        DatosHospedaje actualizado = hospedajeService.actualizarHospedaje(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/hospedajes/{id} (Eliminar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        hospedajeService.eliminarHospedaje(id);
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Hospedaje eliminado con éxito"));
    }
}