package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.ProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.service.ProfessorService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    // Cadastrar professor
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(
            @Valid @RequestBody ProfessorCreateDTO professorCreateDTO) {

        ProfessorResponseDTO professor = professorService.cadastrarProfessor(professorCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    // Editar professor
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

    // Buscar professor por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        ProfessorResponseDTO professor = professorService.buscarPorId(id);
        return ResponseEntity.ok(professor);
    }
}