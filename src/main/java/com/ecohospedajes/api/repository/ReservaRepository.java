package com.ecohospedajes.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecohospedajes.api.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByHospedajeId(Long hospedajeId);

    @Query("SELECT r FROM Reserva r WHERE r.hospedaje.id = :hospedajeId " +
            "AND r.estado = 'CONFIRMADA' " +
            "AND (:fechaEntrada < r.checkout AND :fechaSalida > r.checkin)")
    List<Reserva> findReservasEnConflicto(@Param("hospedajeId") Long hospedajeId,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida);
}