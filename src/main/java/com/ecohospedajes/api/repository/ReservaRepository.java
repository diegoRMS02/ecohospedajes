package com.ecohospedajes.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecohospedajes.api.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId); // Mis viajes

    List<Reserva> findByHospedajeId(Long hospedajeId); // Reservas de mi hotel
}