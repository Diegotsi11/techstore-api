package cl.techstore.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para permitir POST/PUT desde Postman
            .cors(Customizer.withDefaults()) // Permite intercambio de recursos de origen cruzado
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/productos/**").permitAll() // Permite acceso total a productos
                .requestMatchers("/api/**").permitAll() // Permite cualquier otra ruta bajo /api/
                .anyRequest().permitAll() // Por ahora, permite todo para que termines tus pruebas sin bloqueos
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Útil si usas consola H2 o similar
        
        return http.build();
    }
}