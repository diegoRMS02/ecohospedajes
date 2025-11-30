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
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/*.html", "/").permitAll()
                        .requestMatchers("/dueno/**").permitAll()
                        .requestMatchers("/admin/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hospedajes/**").permitAll()

                        // 👉 PERMITIR PAGOS
                        .requestMatchers("/payments/**").permitAll()

                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/hospedajes").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.PUT, "/api/hospedajes/**").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.DELETE, "/api/hospedajes/**").hasAuthority("DUENO")
                        .requestMatchers(HttpMethod.GET, "/api/hospedajes/dueno/**").hasAuthority("DUENO")
                        .requestMatchers("/api/dashboard/**").hasAuthority("DUENO")

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