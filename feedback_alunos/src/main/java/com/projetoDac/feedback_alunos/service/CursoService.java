package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.mapper.CursoMapper;
import com.projetoDac.feedback_alunos.exception.CursoJaExisteException;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.repository.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso cadastrarCurso(CursoCreateDTO cursoCreateDTO) {
        if (cursoRepository.existsByNome(cursoCreateDTO.getNome())) {
            throw new CursoJaExisteException("Já existe um curso com este nome. Por favor, escolha um nome diferente.");
        }
        Curso curso = CursoMapper.toEntity(cursoCreateDTO);
        return cursoRepository.save(curso);
    }

    public Curso editarCurso(Long id, CursoCreateDTO cursoCreateDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + id));

        if (!curso.getNome().equals(cursoCreateDTO.getNome()) &&
                cursoRepository.existsByNome(cursoCreateDTO.getNome())) {
            throw new CursoJaExisteException("Já existe um curso com este nome. Por favor, escolha um nome diferente.");
        }

        curso.setNome(cursoCreateDTO.getNome());
        return cursoRepository.save(curso);
    }

    public void excluirCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new CursoNotFoundException("Curso não encontrado com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + id));
    }
}