package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.service.AvaliacaoProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}