package com.ecohospedajes.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecohospedajes.api.entity.Hospedaje;

public interface HospedajeRepository extends JpaRepository<Hospedaje, Long> {
    // Buscar hoteles de un dueño específico
    List<Hospedaje> findByPropietarioId(Long propietarioId);
}