// en PagoController.java (arriba)
package com.ecohospedajes.api.controller;

// <-- CORRIGE aquí si tu servicio está en "servicio"
import com.ecohospedajes.api.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




@Controller
@RequestMapping("/payments")
public class PagoController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/pago-form")
    public String mostrarFormularioPago() {
        return "pago_form"; // debe estar en templates/
    }

    @PostMapping("/create")
    @ResponseBody
    public String createPayment(
            @RequestParam Double amount,
            @RequestParam String method,
            @RequestParam Long reservationId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String cardNumber,
            @RequestParam String expiryDate,
            @RequestParam String cvv
    ) {

        try {
            paymentService.createPayment(
                    reservationId, amount, method,
                    firstName, lastName, email, phone,
                    cardNumber, expiryDate, cvv
            );
            return "OK";

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}

