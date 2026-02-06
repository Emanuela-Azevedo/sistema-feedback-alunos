package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoProfessorMapper;
import com.projetoDac.feedback_alunos.exception.AvaliacaoNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoProfessorService {

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AvaliacaoProfessor criarAvaliacaoProfessor(AvaliacaoProfessorCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Usuario professor = usuarioRepository.findById(dto.getProfessorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Professor não encontrado com ID: " + dto.getProfessorId()));

        AvaliacaoProfessor avaliacao = AvaliacaoProfessorMapper.toEntity(dto);
        avaliacao.setUsuario(usuario);
        avaliacao.setProfessor(professor);

        return avaliacaoProfessorRepository.save(avaliacao);
    }

    public List<AvaliacaoProfessorResponseDTO> listarAvaliacoesProfessor() {

        List<AvaliacaoProfessor> avaliacoes = avaliacaoProfessorRepository.findAll();

        return avaliacoes.stream()
                .map(avaliacao -> new AvaliacaoProfessorResponseDTO(
                        avaliacao.getId(),

                        avaliacao.getUsuario().getIdUsuario(),
                        avaliacao.isAnonima() ? null : avaliacao.getUsuario().getNome(),

                        avaliacao.getProfessor().getIdUsuario(),
                        avaliacao.getProfessor().getNome(),

                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getDataAvaliacao(),
                        avaliacao.isAnonima()
                ))
                .toList();
    }


    public AvaliacaoProfessor buscarPorId(Long id) {
        return avaliacaoProfessorRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id));
    }

    public void excluirAvaliacao(Long id) {
        if (!avaliacaoProfessorRepository.existsById(id)) {
            throw new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id);
        }
        avaliacaoProfessorRepository.deleteById(id);
    }

    public boolean isAutorDaAvaliacao(Long avaliacaoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String matriculaLogada = authentication.getName(); // vem do JWT subject

        AvaliacaoProfessor avaliacao = avaliacaoProfessorRepository.findById(avaliacaoId)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + avaliacaoId));

        return avaliacao.getUsuario().getMatricula().equals(matriculaLogada);
    }

    public AvaliacaoProfessor atualizarAvaliacao(Long id, AvaliacaoProfessorCreateDTO dto) {
        AvaliacaoProfessor avaliacao = avaliacaoProfessorRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id));

        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setAnonima(dto.isAnonima());

        return avaliacaoProfessorRepository.save(avaliacao);
    }
}