package com.ecohospedajes.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarErroresLogicos(RuntimeException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", ex.getMessage()); // Aquí va el texto "¡Lo sentimos!..."

        // Devolvemos 400 Bad Request en vez de 500 (que asusta)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}