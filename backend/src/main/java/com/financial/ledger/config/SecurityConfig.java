package com.financial.ledger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})              // ✅ enable CORS
            .csrf(csrf -> csrf.disable())  // ❌ disable CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",
                    "/manifest.json",
                    "/favicon.ico"
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
