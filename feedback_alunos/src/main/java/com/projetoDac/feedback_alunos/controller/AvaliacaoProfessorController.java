package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.service.AvaliacaoProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes/professores")
public class AvaliacaoProfessorController {

    @Autowired
    private AvaliacaoProfessorService avaliacaoProfessorService;

    @PostMapping
    public ResponseEntity<AvaliacaoProfessorResponseDTO> criarAvaliacaoProfessor(@Valid @RequestBody AvaliacaoProfessorCreateDTO avaliacaoCreateDTO) {
        AvaliacaoProfessorResponseDTO avaliacao = avaliacaoProfessorService.criarAvaliacaoProfessor(avaliacaoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoProfessorResponseDTO>> listarAvaliacoes() {
        List<AvaliacaoProfessorResponseDTO> avaliacoes = avaliacaoProfessorService.listarAvaliacoes();
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoProfessorResponseDTO avaliacao = avaliacaoProfessorService.buscarPorId(id);
        return ResponseEntity.ok(avaliacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoProfessorService.excluirAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}