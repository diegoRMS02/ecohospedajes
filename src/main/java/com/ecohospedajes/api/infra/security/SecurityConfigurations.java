package com.ecohospedajes.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. RECURSOS ESTÁTICOS (HTML, CSS, JS, IMAGES) -> PERMITIR TODO
                        // Esto permite cargar las VISTAS. La data se protege en la API.
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/*.html", "/").permitAll()
                        .requestMatchers("/dueno/**").permitAll() // <--- AGREGADO: Permite ver la carpeta dueno
                        .requestMatchers("/admin/**").permitAll() // <--- AGREGADO: Permite ver la carpeta admin

                        // 2. RUTAS DE AUTENTICACIÓN (API)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()

                        // 3. RUTAS PÚBLICAS DE API (Catálogo)
                        .requestMatchers(HttpMethod.GET, "/api/hospedajes/**").permitAll()

                        // 4. RUTAS PROTEGIDAS POR ROL (API)
                        // Admin
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        // Dueño
                        .requestMatchers(HttpMethod.POST, "/api/hospedajes").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.PUT, "/api/hospedajes/**").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.DELETE, "/api/hospedajes/**").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.GET, "/api/hospedajes/dueno/**").hasAuthority("DUENO")
                        .requestMatchers("/api/dashboard/**").hasAuthority("DUENO")

                        // 5. RESTO BLOQUEADO (Reservas, Perfil, etc.)
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}