package com.projetoDac.feedback_alunos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.service.RoleService;
import com.projetoDac.feedback_alunos.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    protected UserService userService;

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody Usuario user) {
        try {
            user.setIdUsuario(null);
            encriptPassword(user);
            userService.save(user);
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    private void encriptPassword(Usuario user) {
        String encodedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encodedPassword);
    }
}