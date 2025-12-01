package com.ecohospedajes.api.dto;

import java.time.LocalDate;

public class ReservaResponse {

    private Long id;
    private String nombreCliente;
    private String nombreHospedaje;
    private LocalDate checkin;
    private LocalDate checkout;
    private Integer personas;
    private Double precioTotal;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreHospedaje() { return nombreHospedaje; }
    public void setNombreHospedaje(String nombreHospedaje) { this.nombreHospedaje = nombreHospedaje; }

    public LocalDate getCheckin() { return checkin; }
    public void setCheckin(LocalDate checkin) { this.checkin = checkin; }

    public LocalDate getCheckout() { return checkout; }
    public void setCheckout(LocalDate checkout) { this.checkout = checkout; }

    public Integer getPersonas() { return personas; }
    public void setPersonas(Integer personas) { this.personas = personas; }

    public Double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(Double precioTotal) { this.precioTotal = precioTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
