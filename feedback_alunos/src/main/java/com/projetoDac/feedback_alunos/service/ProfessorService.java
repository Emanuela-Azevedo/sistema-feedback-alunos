package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.ProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.ProfessorMapper;
import com.projetoDac.feedback_alunos.exception.ProfessorNotFoundException;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    // Cadastrar
    public ProfessorResponseDTO cadastrarProfessor(ProfessorCreateDTO professorCreateDTO) {
        Professor professor = ProfessorMapper.toEntity(professorCreateDTO);
        Professor salvo = professorRepository.save(professor);
        return ProfessorMapper.toDTO(salvo);
    }

    // Editar
    public ProfessorResponseDTO editarProfessor(Long id, ProfessorCreateDTO professorCreateDTO) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + id));

        professor.setNome(professorCreateDTO.getNome());
        professor.setEspecialidade(professorCreateDTO.getEspecialidade());
        professor.setMatricula(professorCreateDTO.getMatricula());

        Professor atualizado = professorRepository.save(professor);
        return ProfessorMapper.toDTO(atualizado);
    }

    // Excluir
    public void excluirProfessor(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ProfessorNotFoundException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    // Listar todos
    public List<ProfessorResponseDTO> listarProfessores() {
        return professorRepository.findAll()
                .stream()
                .map(ProfessorMapper::toDTO)
                .toList();
    }

    // Buscar por ID
    public ProfessorResponseDTO buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + id));
        return ProfessorMapper.toDTO(professor);
    }
}
