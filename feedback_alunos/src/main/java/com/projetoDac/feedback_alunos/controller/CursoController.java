package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;

import java.util.List;
import com.projetoDac.feedback_alunos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoResponseDTO> cadastrarCurso(@Valid @RequestBody CursoCreateDTO cursoCreateDTO) {
        CursoResponseDTO curso = cursoService.cadastrarCurso(cursoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> editarCurso(@PathVariable Long id, @Valid @RequestBody CursoCreateDTO cursoCreateDTO) {
        CursoResponseDTO curso = cursoService.editarCurso(id, cursoCreateDTO);
        return ResponseEntity.ok(curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCurso(@PathVariable Long id) {
        cursoService.excluirCurso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listarCursos() {
        List<CursoResponseDTO> cursos = cursoService.listarCursos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Long id) {
        CursoResponseDTO curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(curso);
    }
}