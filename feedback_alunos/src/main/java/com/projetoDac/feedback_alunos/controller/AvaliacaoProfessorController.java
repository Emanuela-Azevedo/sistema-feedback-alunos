package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoProfessorMapper;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.service.AvaliacaoProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes/professores")
public class AvaliacaoProfessorController {

    @Autowired
    private AvaliacaoProfessorService avaliacaoProfessorService;

    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping
    public ResponseEntity<AvaliacaoProfessorResponseDTO> criarAvaliacaoProfessor(
            @Valid @RequestBody AvaliacaoProfessorCreateDTO dto) {

        AvaliacaoProfessor avaliacao = avaliacaoProfessorService.criarAvaliacaoProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(AvaliacaoProfessorMapper.toDTO(avaliacao));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or hasRole('ALUNO')")
    public ResponseEntity<List<AvaliacaoProfessorResponseDTO>> listarAvaliacoes() {
        List<AvaliacaoProfessorResponseDTO> response = avaliacaoProfessorService.listarAvaliacoes().stream()
                .map(AvaliacaoProfessorMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or hasRole('ALUNO')")
    public ResponseEntity<AvaliacaoProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoProfessor avaliacao = avaliacaoProfessorService.buscarPorId(id);
        return ResponseEntity.ok(AvaliacaoProfessorMapper.toDTO(avaliacao));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ALUNO') and @avaliacaoProfessorService.isAutorDaAvaliacao(#id))")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoProfessorService.excluirAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}