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

        public ReservaService(ReservaRepository reservaRepository, HospedajeRepository hospedajeRepository,
                        UsuarioRepository usuarioRepository) {
                this.reservaRepository = reservaRepository;
                this.hospedajeRepository = hospedajeRepository;
                this.usuarioRepository = usuarioRepository;
        }

        public DatosReserva crearReserva(DatosReserva datos) {
                // 1. VALIDACIÓN ESTRICTA: La salida debe ser DESPUÉS de la entrada (no el mismo
                // día)
                if (!datos.getCheckout().isAfter(datos.getCheckin())) {
                        throw new RuntimeException("La fecha de salida debe ser al menos un día después de la entrada");
                }

                // --- VALIDACIÓN DE DISPONIBILIDAD (OVERBOOKING) ---
                List<Reserva> conflictos = reservaRepository.findReservasEnConflicto(
                                datos.getHospedajeId(),
                                datos.getCheckin(),
                                datos.getCheckout());

                if (!conflictos.isEmpty()) {
                        throw new RuntimeException("¡Lo sentimos! Esas fechas ya están ocupadas.");
                }
                // --------------------------------------------------

                // 2. Buscar Hospedaje
                Hospedaje hospedaje = hospedajeRepository.findById(datos.getHospedajeId())
                                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));

                // 3. Buscar Usuario
                Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // 4. Calcular precio
                long dias = ChronoUnit.DAYS.between(datos.getCheckin(), datos.getCheckout());
                if (dias < 1)
                        dias = 1;
                double total = hospedaje.getPrecio() * dias;

                // 5. Guardar la reserva
                Reserva reserva = new Reserva();
                reserva.setCheckin(datos.getCheckin());
                reserva.setCheckout(datos.getCheckout());
                reserva.setPersonas(datos.getPersonas());
                reserva.setPrecioTotal(total);
                reserva.setHospedaje(hospedaje);
                reserva.setUsuario(usuario);
                reserva.setEstado("CONFIRMADA");

                Reserva guardada = reservaRepository.save(reserva);

                // 6. Retornar datos actualizados
                datos.setId(guardada.getId());
                datos.setPrecioTotal(total);

                return datos;
        }

        public List<Reserva> listarReservasDeUsuario(Long usuarioId) {
                return reservaRepository.findByUsuarioId(usuarioId);
        }
}