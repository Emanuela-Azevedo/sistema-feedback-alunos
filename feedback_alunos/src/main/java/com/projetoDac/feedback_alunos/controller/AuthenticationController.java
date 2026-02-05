package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.controller.exception.ErrorMessage;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.jwt.JwtToken;
import com.projetoDac.feedback_alunos.jwt.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UsuarioLoginDTO dto, HttpServletRequest request) {
        log.info("Iniciando login para matrícula {}", dto.getMatricula());
        log.info("Senha recebida do front: {}", dto.getPassword()); // ⚠️ Apenas para debug, remover em produção

        try {
            // tenta autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getMatricula(), dto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtToken token = userDetailsService.getTokenAuthenticated(dto.getMatricula());
            log.info("Login bem-sucedido para matrícula {}", dto.getMatricula());
            log.info("Token gerado: {}", token.getToken()); // log do token, apenas para debug

            return ResponseEntity.ok(token);

        } catch (AuthenticationException e) {
            log.error("Falha de autenticação para matrícula '{}': {}", dto.getMatricula(), e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        log.info("Logout realizado com sucesso");
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}