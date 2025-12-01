package com.ecohospedajes.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.dto.DatosReserva;
import com.ecohospedajes.api.dto.ReservaResponse;
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
    public ResponseEntity<DatosReserva> reservar(@Valid @RequestBody DatosReserva datos,
            @AuthenticationPrincipal Usuario usuarioLogueado) {

        datos.setUsuarioId(usuarioLogueado.getId());
        return ResponseEntity.ok(reservaService.crearReserva(datos));
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> obtenerMisViajes(@AuthenticationPrincipal Usuario usuarioLogueado) {
        return ResponseEntity.ok(reservaService.listarReservasDeUsuario(usuarioLogueado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada con éxito"));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
        @PathVariable Long id,
        @RequestBody Map<String, String> body) {

    String nuevoEstado = body.get("estado");

    reservaService.cambiarEstado(id, nuevoEstado);

    return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado con éxito"));
}

}
