package com.financial.ledger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ Configure CORS with Spring Security 6
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ✅ Disable CSRF (stateless REST APIs)
            .csrf(csrf -> csrf.disable())

            // ✅ Stateless session policy for JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ Authorization rules - permit public access to specified paths
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",                     // React root
                    "/index.html",           // SPA entry
                    "/manifest.json",        // PWA manifest
                    "/favicon.ico",          // Favicon
                    "/static/**",            // Static assets
                    "/api/auth/**",          // Login / Register endpoints
                    "/actuator/health"       // Railway health check
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Allow specific origin only (no wildcard, no preview URLs)
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // ✅ Allow common HTTP methods including OPTIONS for preflight
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Allow common headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));

        // ✅ Allow credentials (for JWT cookies if needed)
        configuration.setAllowCredentials(true);

        // ✅ Expose common headers to frontend
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
