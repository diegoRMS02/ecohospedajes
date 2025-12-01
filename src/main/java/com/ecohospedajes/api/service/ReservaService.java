package com.ecohospedajes.api.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecohospedajes.api.dto.DatosReserva;
import com.ecohospedajes.api.dto.ReservaResponse;
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

    public ReservaService(ReservaRepository reservaRepository,
                          HospedajeRepository hospedajeRepository,
                          UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.hospedajeRepository = hospedajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DatosReserva crearReserva(DatosReserva datos) {

        // Validar fechas
        if (!datos.getCheckout().isAfter(datos.getCheckin())) {
            throw new RuntimeException("La fecha de salida debe ser después de la fecha de entrada.");
        }

        public DatosReserva crearReserva(DatosReserva datos) {
                if (!datos.getCheckout().isAfter(datos.getCheckin())) {
                        throw new RuntimeException("La fecha de salida debe ser al menos un día después de la entrada");
                }

                List<Reserva> conflictos = reservaRepository.findReservasEnConflicto(
                                datos.getHospedajeId(),
                                datos.getCheckin(),
                                datos.getCheckout());

                if (!conflictos.isEmpty()) {
                        throw new RuntimeException("¡Lo sentimos! Esas fechas ya están ocupadas.");
                }

                Hospedaje hospedaje = hospedajeRepository.findById(datos.getHospedajeId())
                                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));

                Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                long dias = ChronoUnit.DAYS.between(datos.getCheckin(), datos.getCheckout());
                if (dias < 1)
                        dias = 1;
                double total = hospedaje.getPrecio() * dias;

                Reserva reserva = new Reserva();
                reserva.setCheckin(datos.getCheckin());
                reserva.setCheckout(datos.getCheckout());
                reserva.setPersonas(datos.getPersonas());
                reserva.setPrecioTotal(total);
                reserva.setHospedaje(hospedaje);
                reserva.setUsuario(usuario);
                reserva.setEstado("CONFIRMADA");

                Reserva guardada = reservaRepository.save(reserva);

                datos.setId(guardada.getId());
                datos.setPrecioTotal(total);

                return datos;
        }

        // Buscar hospedaje
        Hospedaje hospedaje = hospedajeRepository.findById(datos.getHospedajeId())
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado."));

        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Calcular noches
        long noches = ChronoUnit.DAYS.between(datos.getCheckin(), datos.getCheckout());

        // Calcular precio total
        double precioTotal = noches * hospedaje.getPrecio();

        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setCheckin(datos.getCheckin());
        reserva.setCheckout(datos.getCheckout());
        reserva.setPersonas(datos.getPersonas());
        reserva.setPrecioTotal(precioTotal);
        reserva.setUsuario(usuario);
        reserva.setHospedaje(hospedaje);
        reserva.setEstado("ACTIVA");
        reservaRepository.save(reserva);

        // Rellenar DTO de respuesta
        datos.setId(reserva.getId());
        datos.setPrecioTotal(precioTotal);

        return datos;
    }


    public List<Reserva> listarReservasDeUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }


    public List<ReservaResponse> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));

        if (java.time.LocalDate.now().isAfter(reserva.getCheckin())) {
            throw new RuntimeException("No se pueden cancelar reservas ya iniciadas o pasadas.");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }


    private ReservaResponse toResponse(Reserva r) {
        ReservaResponse resp = new ReservaResponse();
                if (java.time.LocalDate.now().isAfter(reserva.getCheckin())) {
                        throw new RuntimeException("No se pueden cancelar reservas pasadas o en curso.");
                }

        resp.setId(r.getId());
        resp.setCheckin(r.getCheckin());
        resp.setCheckout(r.getCheckout());
        resp.setPersonas(r.getPersonas());
        resp.setPrecioTotal(r.getPrecioTotal());
        resp.setEstado(r.getEstado());

        if (r.getUsuario() != null) {
            String nombre = r.getUsuario().getNombre();
            String apellido = r.getUsuario().getApellidos() != null ? r.getUsuario().getApellidos() : "";
            resp.setNombreCliente((nombre + " " + apellido).trim());
        }

        if (r.getHospedaje() != null) {
            resp.setNombreHospedaje(r.getHospedaje().getNombre());
        }

        return resp;
    }
}
        public void cambiarEstado(Long id, String nuevoEstado) {
    Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    reserva.setEstado(nuevoEstado);
    reservaRepository.save(reserva);
}

}
