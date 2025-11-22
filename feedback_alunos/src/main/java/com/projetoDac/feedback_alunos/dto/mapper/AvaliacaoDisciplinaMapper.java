package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import org.modelmapper.ModelMapper;

public class AvaliacaoDisciplinaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static AvaliacaoDisciplina toEntity(AvaliacaoDisciplinaCreateDTO dto){
        return mapper.map(dto, AvaliacaoDisciplina.class);
    }
    public static AvaliacaoDisciplinaResponseDTO toDTO(AvaliacaoDisciplina avaliacao){
        AvaliacaoDisciplinaResponseDTO dto = mapper.map(avaliacao, AvaliacaoDisciplinaResponseDTO.class);
        if (avaliacao.getUsuario() != null && !avaliacao.isAnonima()) {
            dto.setUsuarioId(avaliacao.getUsuario().getIdUsuario());
        } else {
            dto.setUsuarioId(null);
        }
        if (avaliacao.getDisciplina() != null) {
            dto.setDisciplinaId(avaliacao.getDisciplina().getIdDisciplina());
        }
        return dto;
    }
}
