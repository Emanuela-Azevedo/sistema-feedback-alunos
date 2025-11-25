package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import org.modelmapper.ModelMapper;

public class AvaliacaoProfessorMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static AvaliacaoProfessor toEntity(AvaliacaoProfessorCreateDTO dto){
        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setAnonima(dto.isAnonima());
        return avaliacao;
    }
    public static AvaliacaoProfessorResponseDTO toDTO(AvaliacaoProfessor avaliacao){
        AvaliacaoProfessorResponseDTO dto = mapper.map(avaliacao, AvaliacaoProfessorResponseDTO.class);
        if (avaliacao.getUsuario() != null && !avaliacao.isAnonima()) {
            dto.setUsuarioId(avaliacao.getUsuario().getIdUsuario());
        } else {
            dto.setUsuarioId(null);
        }
        if (avaliacao.getProfessor() != null) {
            dto.setProfessorId(avaliacao.getProfessor().getIdUsuario());
        }
        return dto;
    }
}
