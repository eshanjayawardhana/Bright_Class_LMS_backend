package com.lms.lms_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    // In application.yml
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Give permission to Angular
        config.setAllowedOrigins(List.of(allowedOrigins));

        // Give permission to all Methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Give permission to all Headers
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Origin", "Accept"));

        // Cookies (Refresh Token)
        config.setAllowCredentials(true);

        // Cache the Preflight (OPTIONS) request for 3600 seconds (one hour) to improve performance.
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
