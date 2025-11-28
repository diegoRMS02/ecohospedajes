package com.ecohospedajes.api.service;

import java.time.temporal.ChronoUnit;

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

    public ReservaService(ReservaRepository reservaRepository, HospedajeRepository hospedajeRepository,
            UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.hospedajeRepository = hospedajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DatosReserva crearReserva(DatosReserva datos) {
        // 1. Validar fechas
        if (datos.getCheckin().isAfter(datos.getCheckout())) {
            throw new RuntimeException("La fecha de salida debe ser después de la entrada");
        }

        // 2. Buscar Hospedaje y Usuario
        Hospedaje hospedaje = hospedajeRepository.findById(datos.getHospedajeId())
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));

        Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. CALCULAR PRECIO REAL (Seguridad)
        long dias = ChronoUnit.DAYS.between(datos.getCheckin(), datos.getCheckout());
        if (dias < 1)
            dias = 1; // Mínimo cobramos 1 noche
        double total = hospedaje.getPrecio() * dias;

        // 4. Guardar
        Reserva reserva = new Reserva();
        reserva.setCheckin(datos.getCheckin());
        reserva.setCheckout(datos.getCheckout());
        reserva.setPersonas(datos.getPersonas());
        reserva.setPrecioTotal(total);
        reserva.setHospedaje(hospedaje);
        reserva.setUsuario(usuario);
        reserva.setEstado("CONFIRMADA");

        Reserva guardada = reservaRepository.save(reserva);

        // 5. Retornar datos con el precio calculado y el ID
        datos.setId(guardada.getId());
        datos.setPrecioTotal(total);

        return datos;
    }
}