package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.service.AvaliacaoDisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes/disciplinas")
public class AvaliacaoDisciplinaController {

    @Autowired
    private AvaliacaoDisciplinaService avaliacaoDisciplinaService;

    @PostMapping
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> criarAvaliacaoDisciplina(@Valid @RequestBody AvaliacaoDisciplinaCreateDTO avaliacaoCreateDTO) {
        AvaliacaoDisciplinaResponseDTO avaliacao = avaliacaoDisciplinaService.criarAvaliacaoDisciplina(avaliacaoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }
}