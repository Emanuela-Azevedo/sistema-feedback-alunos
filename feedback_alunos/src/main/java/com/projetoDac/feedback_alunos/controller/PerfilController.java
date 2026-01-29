package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.PerfilMapper;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerfilResponseDTO> cadastrarPerfil(@Valid @RequestBody PerfilCreateDTO dto) {
        Perfil perfil = perfilService.cadastrarPerfil(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(PerfilMapper.toDTO(perfil));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerfilResponseDTO> editarPerfil(@PathVariable Long id, @Valid @RequestBody PerfilCreateDTO dto) {
        Perfil perfil = perfilService.editarPerfil(id, dto);
        return ResponseEntity.ok(PerfilMapper.toDTO(perfil));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        perfilService.excluirPerfil(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PerfilResponseDTO>> listarPerfis() {
        List<PerfilResponseDTO> response = perfilService.listarPerfis().stream()
                .map(PerfilMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerfilResponseDTO> buscarPorId(@PathVariable Long id) {
        Perfil perfil = perfilService.buscarPorId(id);
        return ResponseEntity.ok(PerfilMapper.toDTO(perfil));
    }

    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerfilResponseDTO> buscarPorNome(@PathVariable String nome) {
        Perfil perfil = perfilService.buscarPorNome(nome);
        return ResponseEntity.ok(PerfilMapper.toDTO(perfil));
    }
}