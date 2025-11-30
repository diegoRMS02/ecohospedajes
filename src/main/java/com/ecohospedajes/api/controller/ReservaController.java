package com.ecohospedajes.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping; // Faltaba este import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.dto.DatosReserva;
import com.ecohospedajes.api.entity.Reserva;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.service.ReservaService;

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
    // Inyectamos al usuario dueño del Token
    public ResponseEntity<DatosReserva> reservar(@Valid @RequestBody DatosReserva datos,
            @AuthenticationPrincipal Usuario usuarioLogueado) {

        // 1. SEGURIDAD: Sobreescribimos el ID del usuario con el del Token
        // Aunque el hacker mande otro ID en el JSON, nosotros usamos el real.
        datos.setUsuarioId(usuarioLogueado.getId());

        return ResponseEntity.ok(reservaService.crearReserva(datos));
    }

    // Cambiamos la ruta para que no dependa de un ID en la URL
    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> obtenerMisViajes(@AuthenticationPrincipal Usuario usuarioLogueado) {
        // Buscamos solo las reservas de ESTE usuario
        return ResponseEntity.ok(reservaService.listarReservasDeUsuario(usuarioLogueado.getId()));
    }

    @DeleteMapping("/{id}") // Cambiado a DeleteMapping por semántica (o PutMapping si es cancelación
                            // lógica)
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok().body(Map.of("mensaje", "Reserva cancelada con éxito"));
    }
}