package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import org.modelmapper.ModelMapper;

public class AvaliacaoDisciplinaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static AvaliacaoDisciplina toEntity(AvaliacaoDisciplinaCreateDTO dto) {
        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setAnonima(dto.isAnonima());
        return avaliacao;
    }

    public static AvaliacaoDisciplinaResponseDTO toDTO(AvaliacaoDisciplina avaliacao) {
        AvaliacaoDisciplinaResponseDTO dto =
                mapper.map(avaliacao, AvaliacaoDisciplinaResponseDTO.class);

        // Usuário (respeitando anonimato)
        if (avaliacao.getUsuario() != null && !avaliacao.isAnonima()) {
            dto.setUsuarioId(avaliacao.getUsuario().getIdUsuario());
            dto.setUsuarioNome(avaliacao.getUsuario().getNome());
        } else {
            dto.setUsuarioId(null);
            dto.setUsuarioNome("Anônimo");
        }

        // Disciplina avaliada
        if (avaliacao.getDisciplina() != null) {
            dto.setDisciplinaId(avaliacao.getDisciplina().getIdDisciplina());
            dto.setUsuarioNome(avaliacao.getDisciplina().getNome());
        }

        return dto;
    }
}