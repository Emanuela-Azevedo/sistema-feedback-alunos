package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> cadastrarDisciplina(@Valid @RequestBody DisciplinaCreateDTO disciplinaCreateDTO) {
        DisciplinaResponseDTO disciplina = disciplinaService.cadastrarDisciplina(disciplinaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> editarDisciplina(@PathVariable Long id, @Valid @RequestBody DisciplinaCreateDTO disciplinaCreateDTO) {
        DisciplinaResponseDTO disciplina = disciplinaService.editarDisciplina(id, disciplinaCreateDTO);
        return ResponseEntity.ok(disciplina);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDisciplina(@PathVariable Long id) {
        disciplinaService.excluirDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> listarDisciplinas() {
        List<DisciplinaResponseDTO> disciplinas = disciplinaService.listarDisciplinas();
        return ResponseEntity.ok(disciplinas);
    }
}