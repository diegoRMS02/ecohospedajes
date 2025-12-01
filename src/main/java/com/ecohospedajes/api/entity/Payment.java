package com.ecohospedajes.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Double amount;

    @Column(name = "metodo")
    private String method;

    @Column(name = "reservacion_id")
    private Long reservationId;

    @Column(name = "nombre")
    private String firstName;

    @Column(name = "apellido")
    private String lastName;

    @Column(name = "correo")
    private String email;

    @Column(name = "telefono")
    private String phone;

    @Column(name = "tarjeta_numero")
    private String cardNumber;

    @Column(name = "fecha_expiracion")
    private String expiryDate;

    @Column(name = "cvv")
    private String cvv;
}
