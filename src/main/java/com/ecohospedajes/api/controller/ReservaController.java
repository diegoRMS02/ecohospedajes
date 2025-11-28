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

import com.ecohospedajes.api.dto.DatosReserva;
import com.ecohospedajes.api.entity.Reserva;
import com.ecohospedajes.api.service.ReservaService; // <--- Importante: Para usar listas

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<DatosReserva> reservar(@Valid @RequestBody DatosReserva datos) {
        return ResponseEntity.ok(reservaService.crearReserva(datos));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> obtenerMisViajes(@PathVariable Long usuarioId) {
        // En el futuro, aquí podríamos agregar filtros (ej: solo viajes futuros)
        return ResponseEntity.ok(reservaService.listarReservasDeUsuario(usuarioId));
    }
}