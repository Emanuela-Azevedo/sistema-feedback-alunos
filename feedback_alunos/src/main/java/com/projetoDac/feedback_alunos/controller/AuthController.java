package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.TokenDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.jwt.JWTService;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    protected AuthenticationManager authenticationManager;
    protected UsuarioCompletoService usuarioCompletoService;
    private final JWTService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UsuarioCompletoService usuarioCompletoService, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioCompletoService = usuarioCompletoService;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO login){
        try{
            Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getMatricula(), login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);

            UsuarioCompletoResponseDTO userDTO = usuarioCompletoService.loadUserByUsername(login.getMatricula());
            TokenDTO tokenDTO = new TokenDTO(token , userDTO);

            return ResponseEntity.ok(tokenDTO);

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}
