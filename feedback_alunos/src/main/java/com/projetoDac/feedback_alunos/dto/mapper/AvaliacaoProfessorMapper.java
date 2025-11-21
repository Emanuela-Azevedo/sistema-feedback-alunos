package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import org.modelmapper.ModelMapper;

public class AvaliacaoProfessorMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static AvaliacaoProfessor toEntity(AvaliacaoProfessorCreateDTO dto){
        return mapper.map(dto, AvaliacaoProfessor.class);
    }
    public static AvaliacaoProfessorResponseDTO toDTO(AvaliacaoProfessor avaliacao){
        return mapper.map(avaliacao, AvaliacaoProfessorResponseDTO.class);
    }
}
