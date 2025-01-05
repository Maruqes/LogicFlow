package LogicFlow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa proteção contra CSRF (recomendado para APIs públicas)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()); // Permite acesso público a todos os endpoints
        return http.build();
    }
}
