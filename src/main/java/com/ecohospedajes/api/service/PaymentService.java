package com.ecohospedajes.api.service;

import com.ecohospedajes.api.entity.Payment;
import com.ecohospedajes.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Firma EXACTA que usa tu controller (reserva, monto, método, nombres, apellidos, email, phone, tarjeta, expiry, cvv)
    public void createPayment(
            Long reservationId,
            Double amount,
            String method,
            String firstName,
            String lastName,
            String email,
            String phone,
            String cardNumber,
            String expiryDate,
            String cvv
    ) {
        Payment payment = new Payment();
        payment.setReservationId(reservationId);
        payment.setAmount(amount);
        payment.setMethod(method);

        payment.setFirstName(firstName);
        payment.setLastName(lastName);
        payment.setEmail(email);
        payment.setPhone(phone);

        // No recomendable guardar CVV en producción; aquí lo hacemos solo para pruebas locales
        payment.setCardNumber(cardNumber);
        payment.setExpiryDate(expiryDate);
        payment.setCvv(cvv);

        paymentRepository.save(payment);
    }
}

