package com.ecohospedajes.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.entity.Hospedaje;
import com.ecohospedajes.api.entity.Reserva;
import com.ecohospedajes.api.repository.HospedajeRepository;
import com.ecohospedajes.api.repository.ReservaRepository;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final HospedajeRepository hospedajeRepository;
    private final ReservaRepository reservaRepository;

    public DashboardController(HospedajeRepository hospedajeRepository, ReservaRepository reservaRepository) {
        this.hospedajeRepository = hospedajeRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/{duenoId}")
    public ResponseEntity<Map<String, Object>> obtenerResumen(@PathVariable Long duenoId) {
        // 1. Buscar todos los hoteles de este dueño
        List<Hospedaje> misHoteles = hospedajeRepository.findByPropietarioId(duenoId);

        List<Reserva> todasLasReservas = new ArrayList<>();
        double gananciasTotales = 0;

        // 2. Por cada hotel, buscar sus reservas y sumar dinero
        for (Hospedaje h : misHoteles) {
            // Buscamos las reservas de ese hotel específico
            List<Reserva> reservasDelHotel = reservaRepository.findByHospedajeId(h.getId());

            // Las agregamos a la lista general para mostrarlas en la tabla
            todasLasReservas.addAll(reservasDelHotel);

            // Sumamos la plata
            for (Reserva r : reservasDelHotel) {
                gananciasTotales += r.getPrecioTotal();
            }
        }

        // 3. Empaquetar todo en un JSON bonito
        Map<String, Object> data = new HashMap<>();
        data.put("totalHoteles", misHoteles.size());
        data.put("totalReservas", todasLasReservas.size());
        data.put("ganancias", gananciasTotales);
        data.put("listaReservas", todasLasReservas); // Para la tabla detallada

        return ResponseEntity.ok(data);
    }
}