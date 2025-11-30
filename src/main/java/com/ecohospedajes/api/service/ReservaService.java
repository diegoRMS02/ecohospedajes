package com.ecohospedajes.api.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecohospedajes.api.dto.DatosReserva;
import com.ecohospedajes.api.entity.Hospedaje;
import com.ecohospedajes.api.entity.Reserva;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.repository.HospedajeRepository;
import com.ecohospedajes.api.repository.ReservaRepository;
import com.ecohospedajes.api.repository.UsuarioRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HospedajeRepository hospedajeRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            HospedajeRepository hospedajeRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.hospedajeRepository = hospedajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DatosReserva crearReserva(DatosReserva datos) {

        // Validar fechas
        if (!datos.getCheckout().isAfter(datos.getCheckin())) {
            throw new RuntimeException("La fecha de salida debe ser después de la fecha de entrada.");
        }

        // Validar disponibilidad → uso del método JPQL de tu repo
        List<Reserva> conflictos = reservaRepository.findReservasEnConflicto(
                datos.getHospedajeId(),
                datos.getCheckin(),
                datos.getCheckout()
        );

        if (!conflictos.isEmpty()) {
            throw new RuntimeException("Las fechas seleccionadas ya están reservadas.");
        }

        // Buscar hospedaje
        Hospedaje hospedaje = hospedajeRepository.findById(datos.getHospedajeId())
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado."));

        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Calcular noches
        long noches = ChronoUnit.DAYS.between(datos.getCheckin(), datos.getCheckout());

        // Calcular precio total (tu entidad usa getPrecio())
        double precioTotal = noches * hospedaje.getPrecio();

        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setCheckin(datos.getCheckin());
        reserva.setCheckout(datos.getCheckout());
        reserva.setPersonas(datos.getPersonas());
        reserva.setPrecioTotal(precioTotal);
        reserva.setUsuario(usuario);
        reserva.setHospedaje(hospedaje);

        reservaRepository.save(reserva);

        // Completar DTO
        datos.setId(reserva.getId());
        datos.setPrecioTotal(precioTotal);

        return datos;
    }

    public List<Reserva> listarReservasDeUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public void cancelarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));

        // No permitir cancelar si ya empezó o terminó
        if (java.time.LocalDate.now().isAfter(reserva.getCheckin())) {
            throw new RuntimeException("No se pueden cancelar reservas ya iniciadas o pasadas.");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }
}
