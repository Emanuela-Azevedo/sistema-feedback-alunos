package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoProfessorMapper;
import com.projetoDac.feedback_alunos.exception.ApenasAlunosPodeAvaliarException;
import com.projetoDac.feedback_alunos.exception.AvaliacaoNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@Service
public class AvaliacaoProfessorService {

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRepository professorRepository;

    public AvaliacaoProfessorResponseDTO criarAvaliacaoProfessor(AvaliacaoProfessorCreateDTO avaliacaoCreateDTO) {
        Usuario usuario = usuarioRepository.findById(avaliacaoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + avaliacaoCreateDTO.getUsuarioId()));

        boolean isAluno = usuario.getPerfis().stream()
                .anyMatch(perfil -> "ALUNO".equals(perfil.getNomePerfil()));
        
        if (!isAluno) {
            throw new ApenasAlunosPodeAvaliarException("Apenas alunos podem fazer avaliações.");
        }

        Usuario professor = professorRepository.findById(avaliacaoCreateDTO.getProfessorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Professor não encontrado com ID: " + avaliacaoCreateDTO.getProfessorId()));

        AvaliacaoProfessor avaliacao = AvaliacaoProfessorMapper.toEntity(avaliacaoCreateDTO);
        avaliacao.setUsuario(usuario);
        avaliacao.setProfessor(professor);

        AvaliacaoProfessor avaliacaoSalva = avaliacaoProfessorRepository.save(avaliacao);
        return AvaliacaoProfessorMapper.toDTO(avaliacaoSalva);
    }

    public List<AvaliacaoProfessorResponseDTO> listarAvaliacoes() {
        return avaliacaoProfessorRepository.findAll().stream()
                .map(AvaliacaoProfessorMapper::toDTO)
                .toList();
    }

    public AvaliacaoProfessorResponseDTO buscarPorId(Long id) {
        AvaliacaoProfessor avaliacao = avaliacaoProfessorRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id));
        return AvaliacaoProfessorMapper.toDTO(avaliacao);
    }

    public void excluirAvaliacao(Long id) {
        if (!avaliacaoProfessorRepository.existsById(id)) {
            throw new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id);
        }
        avaliacaoProfessorRepository.deleteById(id);
    }
}