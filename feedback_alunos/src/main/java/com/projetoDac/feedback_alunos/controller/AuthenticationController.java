package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.controller.exception.ErrorMessage;
import com.projetoDac.feedback_alunos.dto.LoginResponseDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.jwt.JwtToken;
import com.projetoDac.feedback_alunos.jwt.JwtUserDetails;
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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getMatricula(), dto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtToken token = userDetailsService.getTokenAuthenticated(dto.getMatricula());

            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

            LoginResponseDTO response = new LoginResponseDTO(
                    token.getToken(),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getNome(),
                    userDetails.getAuthorities().iterator().next().getAuthority()
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
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