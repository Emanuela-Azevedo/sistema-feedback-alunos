package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.UsuarioCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioResponseDTO;
import com.projetoDac.feedback_alunos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Cadastrar usuário
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(
            @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {

        UsuarioResponseDTO usuario = usuarioService.cadastrarUsuario(usuarioCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    // Editar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> editarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {

        UsuarioResponseDTO usuario = usuarioService.editarUsuario(id, usuarioCreateDTO);
        return ResponseEntity.ok(usuario);
    }

    // Excluir usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
