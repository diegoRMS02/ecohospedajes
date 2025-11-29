package com.ecohospedajes.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecohospedajes.api.entity.Hospedaje;

public interface HospedajeRepository extends JpaRepository<Hospedaje, Long> {

    List<Hospedaje> findByPropietarioId(Long propietarioId);

    // FILTRO CON PAGINACIÓN Y BÚSQUEDA DINÁMICA
    @Query("SELECT h FROM Hospedaje h WHERE " +
            "(:ubicacion IS NULL OR h.ubicacion LIKE %:ubicacion%) AND " +
            "(h.precio >= :minPrice AND h.precio <= :maxPrice)")
    Page<Hospedaje> buscarConFiltros(@Param("ubicacion") String ubicacion,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable); // Devuelve una página de resultados
}