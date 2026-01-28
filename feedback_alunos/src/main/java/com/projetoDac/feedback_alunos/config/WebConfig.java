package com.projetoDac.feedback_alunos.config;

import com.projetoDac.feedback_alunos.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public WebConfig(JwtAuthFilter jwtAuthFilter,
                     AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        //  AUTH
                        .requestMatchers("/api/auth/**").permitAll()

                        // ADMINISTRADORES
                        .requestMatchers("/administradores/**").hasRole("ADMIN")

                        // PERFIS
                        .requestMatchers("/perfis/**").hasRole("ADMIN")

                        // USUÁRIOS
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")

                        // ALUNOS
                        .requestMatchers(HttpMethod.POST, "/alunos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/alunos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/alunos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/alunos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/alunos/matricula/**")
                        .hasAnyRole("ADMIN", "ALUNO")

                        // PROFESSORES
                        .requestMatchers(HttpMethod.POST, "/professores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/professores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/professores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/professores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/professores/matricula/**")
                        .hasAnyRole("ADMIN", "PROFESSOR")

                        // DISCIPLINAS
                        .requestMatchers(HttpMethod.POST, "/api/disciplinas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/disciplinas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/disciplinas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/disciplinas/**")
                        .hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")

                        // CURSOS
                        .requestMatchers(HttpMethod.POST, "/api/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/cursos/**")
                        .hasAnyRole("ADMIN", "ALUNO", "PROFESSOR")

                        // CONSULTAS
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**")
                        .hasAnyRole("ADMIN")

                        // 🔐 QUALQUER OUTRA
                        .anyRequest().authenticated()
                );

        return http.build();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}