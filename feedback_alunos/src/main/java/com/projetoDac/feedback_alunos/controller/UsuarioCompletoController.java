package com.projetoDac.feedback_alunos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios-completo")
public class UsuarioCompletoController {

    @Autowired
    private UsuarioCompletoService usuarioCompletoService;

    @PostMapping
    public ResponseEntity<UsuarioCompletoResponseDTO> criarUsuario(
            @Valid @RequestBody UsuarioCompletoCreateDTO dto) {
        UsuarioCompletoResponseDTO usuario = usuarioCompletoService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioCompletoResponseDTO>> listarUsuarios() {
        List<UsuarioCompletoResponseDTO> usuarios = usuarioCompletoService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioCompletoResponseDTO usuario = usuarioCompletoService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioCompletoCreateDTO dto) {
        UsuarioCompletoResponseDTO usuario = usuarioCompletoService.atualizarUsuario(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioCompletoService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }
}