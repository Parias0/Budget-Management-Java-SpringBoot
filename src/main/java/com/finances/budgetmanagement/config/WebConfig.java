package com.finances.budgetmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Ustawienie reguł CORS
        registry.addMapping("/**")  // Ścieżka, na którą pozwalamy
                .allowedOrigins("http://localhost:8080")  // Frontend, który ma dostęp do API
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Dozwolone metody
                .allowedHeaders("*")  // Dozwolone nagłówki
                .allowCredentials(true);  // Możliwość wysyłania ciasteczek i autentykacji
    }
}
