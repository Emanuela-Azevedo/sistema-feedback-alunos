package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.DisciplinaMapper;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.exception.DisciplinaNotFoundException;
import com.projetoDac.feedback_alunos.exception.ProfessorNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public DisciplinaResponseDTO cadastrarDisciplina(DisciplinaCreateDTO disciplinaCreateDTO) {
        Curso curso = cursoRepository.findById(disciplinaCreateDTO.getCursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + disciplinaCreateDTO.getCursoId()));
        
        Professor professor = professorRepository.findById(disciplinaCreateDTO.getProfessorId())
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + disciplinaCreateDTO.getProfessorId()));

        Disciplina disciplina = DisciplinaMapper.toEntity(disciplinaCreateDTO);
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
        
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
        return DisciplinaMapper.toDTO(disciplinaSalva);
    }

    public DisciplinaResponseDTO editarDisciplina(Long id, DisciplinaCreateDTO disciplinaCreateDTO) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id));

        Curso curso = cursoRepository.findById(disciplinaCreateDTO.getCursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + disciplinaCreateDTO.getCursoId()));
        
        Professor professor = professorRepository.findById(disciplinaCreateDTO.getProfessorId())
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + disciplinaCreateDTO.getProfessorId()));

        disciplina.setNome(disciplinaCreateDTO.getNome());
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
        
        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return DisciplinaMapper.toDTO(disciplinaAtualizada);
    }

    public void excluirDisciplina(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id);
        }
        disciplinaRepository.deleteById(id);
    }

    public List<DisciplinaResponseDTO> listarDisciplinas() {
        return disciplinaRepository.findAll().stream()
                .map(DisciplinaMapper::toDTO)
                .toList();
    }

    public DisciplinaResponseDTO buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id));
        return DisciplinaMapper.toDTO(disciplina);
    }
}