package com.ecohospedajes.api.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(@PathVariable Long duenoId) {
        List<Hospedaje> misHoteles = hospedajeRepository.findByPropietarioId(duenoId);

        List<Reserva> todasReservas = new ArrayList<>();
        double gananciasTotales = 0;

        // Contadores Nuevos
        long cantConfirmadas = 0;
        long cantCanceladas = 0;

        for (Hospedaje h : misHoteles) {
            List<Reserva> reservasDelHotel = reservaRepository.findByHospedajeId(h.getId());
            todasReservas.addAll(reservasDelHotel);

            for (Reserva r : reservasDelHotel) {
                if ("CANCELADA".equals(r.getEstado())) {
                    cantCanceladas++;
                } else {
                    // Asumimos que si no es cancelada, es confirmada (o pendiente)
                    cantConfirmadas++;
                    gananciasTotales += r.getPrecioTotal();
                }
            }
        }

        // ... (Lógica de gráficos de barras se mantiene igual) ...
        Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
        LocalDate hoy = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            Month mes = hoy.minusMonths(i).getMonth();
            ingresosPorMes.put(mes.toString(), 0.0);
        }
        for (Reserva r : todasReservas) {
            if (!"CANCELADA".equals(r.getEstado())) {
                String mesReserva = r.getCheckin().getMonth().toString();
                if (ingresosPorMes.containsKey(mesReserva)) {
                    ingresosPorMes.put(mesReserva, ingresosPorMes.get(mesReserva) + r.getPrecioTotal());
                }
            }
        }
        List<String> etiquetasGrafico = new ArrayList<>(ingresosPorMes.keySet());
        List<Double> valoresGrafico = new ArrayList<>(ingresosPorMes.values());

        // Armar Respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("kpi_hoteles", misHoteles.size());
        response.put("kpi_reservas", cantConfirmadas); // Solo las válidas para el KPI
        response.put("kpi_ganancias", gananciasTotales);

        // ENVIAMOS LOS CONTEOS EXACTOS PARA EL GRÁFICO CIRCULAR
        response.put("total_confirmadas", cantConfirmadas);
        response.put("total_canceladas", cantCanceladas);

        response.put("grafico_etiquetas", etiquetasGrafico);
        response.put("grafico_valores", valoresGrafico);

        List<Reserva> ultimasReservas = todasReservas.stream()
                .sorted(Comparator.comparing(Reserva::getId).reversed())
                .limit(10)
                .collect(Collectors.toList());
        response.put("tabla_reservas", ultimasReservas);

        return ResponseEntity.ok(response);
    }
}