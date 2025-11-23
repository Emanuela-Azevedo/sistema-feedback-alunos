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

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.service.AlunoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    // Cadastrar aluno completo
    @PostMapping
    public ResponseEntity<?> cadastrarAluno(
            @Valid @RequestBody AlunoCreateDTO alunoCreateDTO) {

        AlunoResponseDTO aluno = alunoService.cadastrarAlunoCompleto(alunoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }

    // Editar aluno
    @PutMapping("/matricula/{matricula}")
    public ResponseEntity<?> editarAluno(
            @PathVariable String matricula,
            @Valid @RequestBody AlunoCreateDTO alunoCreateDTO) {

        AlunoResponseDTO aluno = alunoService.editarAluno(matricula, alunoCreateDTO);
        return ResponseEntity.ok(aluno);
    }

    // Excluir aluno
    @DeleteMapping("/matricula/{matricula}")
    public ResponseEntity<Void> excluirAluno(@PathVariable String matricula) {
        alunoService.excluirAluno(matricula);
        return ResponseEntity.noContent().build();
    }

    // Listar todos alunos
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        List<AlunoResponseDTO> alunos = alunoService.listarAlunos();
        return ResponseEntity.ok(alunos);
    }

    // Buscar aluno por matrícula
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlunoResponseDTO> buscarPorMatricula(@PathVariable String matricula) {
        AlunoResponseDTO aluno = alunoService.buscarPorMatricula(matricula);
        return ResponseEntity.ok(aluno);
    }
}
