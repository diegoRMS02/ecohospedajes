package com.ecohospedajes.api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public class DatosReserva {

    private Long id;

    @NotNull(message = "La fecha de entrada es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;

    @NotNull(message = "La fecha de salida es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;

    @NotNull(message = "Indica el número de personas")
    private Integer personas;

    @NotNull(message = "Falta el ID del hospedaje")
    private Long hospedajeId;

    private Long usuarioId;

    private Double precioTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public Integer getPersonas() {
        return personas;
    }

    public void setPersonas(Integer personas) {
        this.personas = personas;
    }

    public Long getHospedajeId() {
        return hospedajeId;
    }

    public void setHospedajeId(Long hospedajeId) {
        this.hospedajeId = hospedajeId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
