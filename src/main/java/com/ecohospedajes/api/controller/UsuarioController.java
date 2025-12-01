package com.ecohospedajes.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecohospedajes.api.dto.DatosActualizacionUsuario;
import com.ecohospedajes.api.dto.DatosJWTToken;
import com.ecohospedajes.api.dto.DatosLogin;
import com.ecohospedajes.api.dto.DatosRegistro;
import com.ecohospedajes.api.entity.Usuario;
import com.ecohospedajes.api.infra.security.TokenService;
import com.ecohospedajes.api.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody @Valid DatosRegistro datos) {
        Usuario nuevo = usuarioService.registrar(datos);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<DatosJWTToken> login(@RequestBody @Valid DatosLogin datos) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(datos.getEmail(), datos.getPassword());

        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id,
            @RequestBody @Valid DatosActualizacionUsuario datos) {
        Usuario actualizado = usuarioService.actualizar(id, datos);
        return ResponseEntity.ok(actualizado);
    }
}