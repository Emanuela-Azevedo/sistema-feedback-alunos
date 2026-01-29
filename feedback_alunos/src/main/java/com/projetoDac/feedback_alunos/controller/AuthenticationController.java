package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.TokenDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.jwt.JwtUserDetailsService;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO login){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getMatricula(), login.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.getTokenAuthenticated(authentication);

            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            UsuarioCompletoResponseDTO userDTO = new UsuarioCompletoResponseDTO();
            userDTO.setNome(userDetails.getUsername());

            return ResponseEntity.ok(new TokenDTO(token, userDTO));

        } catch(Exception e){
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}