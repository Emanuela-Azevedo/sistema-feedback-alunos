package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AvaliacaoDisciplinaMapper;
import com.projetoDac.feedback_alunos.exception.DisciplinaNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoDisciplinaService {

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public AvaliacaoDisciplinaResponseDTO criarAvaliacaoDisciplina(AvaliacaoDisciplinaCreateDTO avaliacaoCreateDTO) {
        Usuario usuario = usuarioRepository.findById(avaliacaoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + avaliacaoCreateDTO.getUsuarioId()));

        Disciplina disciplina = disciplinaRepository.findById(avaliacaoCreateDTO.getDisciplinaId())
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada com ID: " + avaliacaoCreateDTO.getDisciplinaId()));

        AvaliacaoDisciplina avaliacao = AvaliacaoDisciplinaMapper.toEntity(avaliacaoCreateDTO);
        avaliacao.setUsuario(usuario);
        avaliacao.setDisciplina(disciplina);

        AvaliacaoDisciplina avaliacaoSalva = avaliacaoDisciplinaRepository.save(avaliacao);
        return AvaliacaoDisciplinaMapper.toDTO(avaliacaoSalva);
    }
}