package com.ecohospedajes.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.dto.DatosHospedaje;
import com.ecohospedajes.api.service.HospedajeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospedajes")
@CrossOrigin(origins = "*")
public class HospedajeController {

    private final HospedajeService hospedajeService;

    public HospedajeController(HospedajeService hospedajeService) {
        this.hospedajeService = hospedajeService;
    }

    @PostMapping
    public ResponseEntity<DatosHospedaje> crear(@Valid @RequestBody DatosHospedaje datos) {
        return ResponseEntity.ok(hospedajeService.guardar(datos));
    }

    @GetMapping
    public ResponseEntity<List<DatosHospedaje>> listar() {
        return ResponseEntity.ok(hospedajeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosHospedaje> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(hospedajeService.buscarPorId(id));
    }
}