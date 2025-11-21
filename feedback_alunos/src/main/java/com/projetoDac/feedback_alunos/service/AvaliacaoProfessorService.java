package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoProfessorMapper;
import com.projetoDac.feedback_alunos.exception.ProfessorNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoProfessorService {

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public AvaliacaoProfessorResponseDTO criarAvaliacaoProfessor(AvaliacaoProfessorCreateDTO avaliacaoCreateDTO) {
        Usuario usuario = usuarioRepository.findById(avaliacaoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + avaliacaoCreateDTO.getUsuarioId()));

        Professor professor = professorRepository.findById(avaliacaoCreateDTO.getProfessorId())
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + avaliacaoCreateDTO.getProfessorId()));

        AvaliacaoProfessor avaliacao = AvaliacaoProfessorMapper.toEntity(avaliacaoCreateDTO);
        avaliacao.setUsuario(usuario);
        avaliacao.setProfessor(professor);

        AvaliacaoProfessor avaliacaoSalva = avaliacaoProfessorRepository.save(avaliacao);
        return AvaliacaoProfessorMapper.toDTO(avaliacaoSalva);
    }
}