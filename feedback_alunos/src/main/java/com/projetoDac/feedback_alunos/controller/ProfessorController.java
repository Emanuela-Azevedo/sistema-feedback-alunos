package com.projetoDac.feedback_alunos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.projetoDac.feedback_alunos.dto.ProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.service.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(
            @Valid @RequestBody ProfessorCreateDTO professorCompletoCreateDTO) {

        ProfessorResponseDTO professor = professorService.cadastrarProfessorCompleto(professorCompletoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> editarProfessor(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorCreateDTO professorCreateDTO) {

        ProfessorResponseDTO professor = professorService.editarProfessor(id, professorCreateDTO);
        return ResponseEntity.ok(professor);
    }

    // Excluir professor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProfessor(@PathVariable Long id) {
        professorService.excluirProfessor(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos professores
    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessores() {
        List<ProfessorResponseDTO> professores = professorService.listarProfessores();
        return ResponseEntity.ok(professores);
    }

    // Buscar professor por matrícula
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorMatricula(@PathVariable String matricula) {
        ProfessorResponseDTO professor = professorService.buscarPorMatricula(matricula);
        return ResponseEntity.ok(professor);
    }
}