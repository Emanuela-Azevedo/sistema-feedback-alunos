package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.service.AlunoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    // Cadastrar aluno
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrarAluno(@Valid @RequestBody AlunoCreateDTO alunoCreateDTO) {
        AlunoResponseDTO aluno = alunoService.cadastrarAluno(alunoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }

    // Cadastro completo (Aluno + Usuario)
    @PostMapping("/completo")
    public ResponseEntity<AlunoResponseDTO> cadastrarAlunoCompleto(@Valid @RequestBody AlunoCreateDTO alunoCreateDTO) {
        AlunoResponseDTO aluno = alunoService.cadastrarAlunoCompleto(alunoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }

    // Editar aluno
    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> editarAluno(@PathVariable Long id,
            @Valid @RequestBody AlunoCreateDTO alunoCreateDTO) {
        AlunoResponseDTO aluno = alunoService.editarAluno(id, alunoCreateDTO);
        return ResponseEntity.ok(aluno);
    }

    // Excluir aluno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluno(@PathVariable Long id) {
        alunoService.excluirAluno(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos os alunos
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        List<AlunoResponseDTO> alunos = alunoService.listarAlunos();
        return ResponseEntity.ok(alunos);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        AlunoResponseDTO aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }

    // Buscar por matrícula
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlunoResponseDTO> buscarPorMatricula(@PathVariable String matricula) {
        AlunoResponseDTO aluno = alunoService.buscarPorMatricula(matricula);
        return ResponseEntity.ok(aluno);
    }
}
