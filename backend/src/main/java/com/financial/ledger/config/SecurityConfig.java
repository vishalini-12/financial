package com.financial.ledger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ Enable CORS using Spring Security 6 style
            .cors(cors -> {})
            
            // ✅ Disable CSRF (stateless REST APIs)
            .csrf(csrf -> csrf.disable())

            // ✅ No session (JWT-based)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",                     // React root
                    "/index.html",           // SPA entry
                    "/manifest.json",        // PWA manifest
                    "/favicon.ico",
                    "/static/**",            // Static assets
                    "/api/auth/**",          // Login / Register
                    "/actuator/health"       // Railway health check
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
