package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoDisciplinaMapper;
import com.projetoDac.feedback_alunos.exception.AvaliacaoNotFoundException;
import com.projetoDac.feedback_alunos.exception.DisciplinaNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvaliacaoDisciplinaService {

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public AvaliacaoDisciplina criarAvaliacaoDisciplina(AvaliacaoDisciplinaCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId())
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + dto.getDisciplinaId()));

        AvaliacaoDisciplina avaliacao = AvaliacaoDisciplinaMapper.toEntity(dto);
        avaliacao.setUsuario(usuario);
        avaliacao.setDisciplina(disciplina);

        return avaliacaoDisciplinaRepository.save(avaliacao);
    }

    public List<AvaliacaoDisciplinaResponseDTO> listarAvaliacoesDisciplina() {

        List<AvaliacaoDisciplina> avaliacoes = avaliacaoDisciplinaRepository.findAll();

        return avaliacoes.stream()
                .map(avaliacao -> new AvaliacaoDisciplinaResponseDTO(
                        avaliacao.getId(),

                        avaliacao.getUsuario().getIdUsuario(),
                        avaliacao.isAnonima() ? null : avaliacao.getUsuario().getNome(),

                        avaliacao.getDisciplina().getIdDisciplina(),
                        avaliacao.getDisciplina().getNome(),

                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getDataAvaliacao(),
                        avaliacao.isAnonima()
                ))
                .toList();
    }

    public AvaliacaoDisciplina buscarPorId(Long id) {
        return avaliacaoDisciplinaRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id));
    }

    public void excluirAvaliacao(Long id) {
        if (!avaliacaoDisciplinaRepository.existsById(id)) {
            throw new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id);
        }
        avaliacaoDisciplinaRepository.deleteById(id);
    }

    public boolean isAutorDaAvaliacao(Long avaliacaoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        String matriculaLogada;
        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario usuarioLogado) {
            matriculaLogada = usuarioLogado.getMatricula();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            matriculaLogada = userDetails.getUsername();
        } else if (principal instanceof String s) {
            matriculaLogada = s;
        } else {
            return false;
        }

        AvaliacaoDisciplina avaliacao = avaliacaoDisciplinaRepository.findById(avaliacaoId)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + avaliacaoId));

        if (avaliacao.getUsuario() == null) {
            return false;
        }
        return avaliacao.getUsuario().getMatricula().equals(matriculaLogada);
    }

    public AvaliacaoDisciplina atualizarAvaliacao(Long id, AvaliacaoDisciplinaCreateDTO dto) {
        AvaliacaoDisciplina avaliacao = avaliacaoDisciplinaRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException("Avaliação não encontrada com ID: " + id));

        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setAnonima(dto.isAnonima());

        return avaliacaoDisciplinaRepository.save(avaliacao);
    }
}