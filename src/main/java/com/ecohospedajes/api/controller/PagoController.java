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
            @RequestParam Double cantidad,
            @RequestParam String metodo,
            @RequestParam Long reservacion_id,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String numero,
            @RequestParam String tarjeta_numero,
            @RequestParam String fecha_expiracion,
            @RequestParam String cvv) {

        try {
            paymentService.createPayment(
                    reservacion_id, cantidad, metodo,
                    nombre, apellidos, correo, numero,
                    tarjeta_numero, fecha_expiracion, cvv);
            return "OK";

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

}
