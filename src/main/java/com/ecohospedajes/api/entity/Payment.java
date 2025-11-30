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

    private Double amount;
    private String method;
    private Long reservationId;

    // Datos del usuario
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // Datos de tarjeta
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}


