package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.CursoMapper;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDTO> cadastrarCurso(@Valid @RequestBody CursoCreateDTO cursoCreateDTO) {
        Curso curso = cursoService.cadastrarCurso(cursoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(CursoMapper.toDTO(curso));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDTO> editarCurso(@PathVariable Long id, @Valid @RequestBody CursoCreateDTO cursoCreateDTO) {
        Curso curso = cursoService.editarCurso(id, cursoCreateDTO);
        return ResponseEntity.ok(CursoMapper.toDTO(curso));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirCurso(@PathVariable Long id) {
        cursoService.excluirCurso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO')")
    public ResponseEntity<List<CursoResponseDTO>> listarCursos() {
        List<CursoResponseDTO> response = cursoService.listarCursos().stream()
                .map(CursoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO')")
    public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(CursoMapper.toDTO(curso));
    }
}