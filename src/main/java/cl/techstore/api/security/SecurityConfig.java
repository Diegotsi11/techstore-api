package cl.techstore.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva la protección contra ataques CSRF (necesario para probar con Postman)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Cambié esto para que permita TODO lo que empiece con /api/
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}