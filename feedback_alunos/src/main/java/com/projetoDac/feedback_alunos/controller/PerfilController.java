package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponseDTO> cadastrarPerfil(@Valid @RequestBody PerfilCreateDTO perfilCreateDTO) {
        PerfilResponseDTO perfil = perfilService.cadastrarPerfil(perfilCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> editarPerfil(@PathVariable Long id, @Valid @RequestBody PerfilCreateDTO perfilCreateDTO) {
        PerfilResponseDTO perfil = perfilService.editarPerfil(id, perfilCreateDTO);
        return ResponseEntity.ok(perfil);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        perfilService.excluirPerfil(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> listarPerfis() {
        List<PerfilResponseDTO> perfis = perfilService.listarPerfis();
        return ResponseEntity.ok(perfis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> buscarPorId(@PathVariable Long id) {
        PerfilResponseDTO perfil = perfilService.buscarPorId(id);
        return ResponseEntity.ok(perfil);
    }
}