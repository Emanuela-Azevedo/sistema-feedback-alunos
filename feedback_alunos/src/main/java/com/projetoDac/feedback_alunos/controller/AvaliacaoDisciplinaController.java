package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoDisciplinaMapper;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import com.projetoDac.feedback_alunos.service.AvaliacaoDisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/avaliacoes/disciplinas")
public class AvaliacaoDisciplinaController {

    @Autowired
    private AvaliacaoDisciplinaService avaliacaoDisciplinaService;

    @PostMapping
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> criarAvaliacaoDisciplina(
            @Valid @RequestBody AvaliacaoDisciplinaCreateDTO dto) {

        AvaliacaoDisciplina avaliacao = avaliacaoDisciplinaService.criarAvaliacaoDisciplina(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(AvaliacaoDisciplinaMapper.toDTO(avaliacao));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or hasRole('ALUNO')")
    public ResponseEntity<List<AvaliacaoDisciplinaResponseDTO>> listarAvaliacoes() {

        List<AvaliacaoDisciplinaResponseDTO> response =
                avaliacaoDisciplinaService.listarAvaliacoesDisciplina();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or hasRole('ALUNO')")
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoDisciplina avaliacao = avaliacaoDisciplinaService.buscarPorId(id);
        return ResponseEntity.ok(AvaliacaoDisciplinaMapper.toDTO(avaliacao));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ALUNO') and @avaliacaoDisciplinaService.isAutorDaAvaliacao(#id))")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoDisciplinaService.excluirAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ALUNO') and @avaliacaoDisciplinaService.isAutorDaAvaliacao(#id)")
    public ResponseEntity<AvaliacaoDisciplinaResponseDTO> atualizarAvaliacao(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoDisciplinaCreateDTO dto) {
        AvaliacaoDisciplina avaliacaoAtualizada = avaliacaoDisciplinaService.atualizarAvaliacao(id, dto);
        return ResponseEntity.ok(AvaliacaoDisciplinaMapper.toDTO(avaliacaoAtualizada));
    }

}