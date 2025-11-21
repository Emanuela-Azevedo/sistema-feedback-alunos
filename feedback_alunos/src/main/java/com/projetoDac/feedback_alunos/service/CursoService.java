package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;

import java.util.List;
import com.projetoDac.feedback_alunos.dto.mapper.CursoMapper;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public CursoResponseDTO cadastrarCurso(CursoCreateDTO cursoCreateDTO) {
        Curso curso = CursoMapper.toEntity(cursoCreateDTO);
        Curso cursoSalvo = cursoRepository.save(curso);
        return CursoMapper.toDTO(cursoSalvo);
    }

    public CursoResponseDTO editarCurso(Long id, CursoCreateDTO cursoCreateDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso não encontrado com ID: " + id));
        curso.setNome(cursoCreateDTO.getNome());
        Curso cursoAtualizado = cursoRepository.save(curso);
        return CursoMapper.toDTO(cursoAtualizado);
    }

    public void excluirCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new CursoNotFoundException("Curso não encontrado com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    public List<CursoResponseDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(CursoMapper::toDTO)
                .toList();
    }
}