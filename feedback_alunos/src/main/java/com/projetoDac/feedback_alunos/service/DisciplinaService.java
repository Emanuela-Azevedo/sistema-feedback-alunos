package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.DisciplinaMapper;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.exception.DisciplinaNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Disciplina cadastrarDisciplina(DisciplinaCreateDTO dto) {
        System.out.println("DTO recebido: " + dto);

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + dto.getCursoId()));

        Usuario professor = usuarioRepository.findById(dto.getProfessorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Professor não encontrado com ID: " + dto.getProfessorId()));

        Disciplina disciplina = DisciplinaMapper.toEntity(dto);
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);

        System.out.println("Disciplina antes do save: curso=" + disciplina.getCurso() + ", professor=" + disciplina.getProfessor());

        return disciplinaRepository.save(disciplina);
    }


    public Disciplina editarDisciplina(Long id, DisciplinaCreateDTO disciplinaCreateDTO) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id));

        Curso curso = cursoRepository.findById(disciplinaCreateDTO.getCursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + disciplinaCreateDTO.getCursoId()));

        Usuario professor = usuarioRepository.findById(disciplinaCreateDTO.getProfessorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Professor não encontrado com ID: " + disciplinaCreateDTO.getProfessorId()));

        disciplina.setNome(disciplinaCreateDTO.getNome());
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);

        return disciplinaRepository.save(disciplina);
    }

    public void excluirDisciplina(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id);
        }
        disciplinaRepository.deleteById(id);
    }

    public List<Disciplina> listarDisciplinas() {
        return disciplinaRepository.findAll();
    }

    public Disciplina buscarPorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + id));
    }
}