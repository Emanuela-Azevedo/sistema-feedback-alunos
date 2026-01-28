package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.service.AvaliacaoDisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes/disciplinas")
public class AvaliacaoDisciplinaController {

    @Autowired
    private AvaliacaoDisciplinaService avaliacaoDisciplinaService;

    @PostMapping
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> criarAvaliacaoDisciplina(
            @Valid @RequestBody AvaliacaoDisciplinaCreateDTO avaliacaoCreateDTO) {

        AvaliacaoDisciplinaResponseDTO avaliacao =
                avaliacaoDisciplinaService.criarAvaliacaoDisciplina(avaliacaoCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoDisciplinaResponseDTO>> listarAvaliacoes() {
        return ResponseEntity.ok(avaliacaoDisciplinaService.listarAvaliacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoDisciplinaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or @avaliacaoDisciplinaService.isAutorDaAvaliacao(#id)"
    )
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoDisciplinaService.excluirAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}