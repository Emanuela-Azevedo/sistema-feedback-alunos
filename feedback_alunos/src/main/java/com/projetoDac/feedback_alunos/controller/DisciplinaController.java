package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.DisciplinaMapper;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisciplinaResponseDTO> cadastrarDisciplina(@Valid @RequestBody DisciplinaCreateDTO dto) {
        Disciplina disciplina = disciplinaService.cadastrarDisciplina(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DisciplinaMapper.toDTO(disciplina));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisciplinaResponseDTO> editarDisciplina(@PathVariable Long id, @Valid @RequestBody DisciplinaCreateDTO dto) {
        Disciplina disciplina = disciplinaService.editarDisciplina(id, dto);
        return ResponseEntity.ok(DisciplinaMapper.toDTO(disciplina));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirDisciplina(@PathVariable Long id) {
        disciplinaService.excluirDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO')")
    public ResponseEntity<List<DisciplinaResponseDTO>> listarDisciplinas() {
        List<DisciplinaResponseDTO> response = disciplinaService.listarDisciplinas().stream()
                .map(DisciplinaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO')")
    public ResponseEntity<DisciplinaResponseDTO> buscarPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);
        return ResponseEntity.ok(DisciplinaMapper.toDTO(disciplina));
    }
}