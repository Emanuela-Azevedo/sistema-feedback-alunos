package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
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
            @Valid @RequestBody AvaliacaoProfessorCreateDTO avaliacaoCreateDTO) {

        AvaliacaoProfessorResponseDTO avaliacao =
                avaliacaoProfessorService.criarAvaliacaoProfessor(avaliacaoCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoProfessorResponseDTO>> listarAvaliacoes() {
        return ResponseEntity.ok(avaliacaoProfessorService.listarAvaliacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoProfessorService.buscarPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('ALUNO') and @avaliacaoProfessorService.isAutorDaAvaliacao(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoProfessorService.excluirAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}
