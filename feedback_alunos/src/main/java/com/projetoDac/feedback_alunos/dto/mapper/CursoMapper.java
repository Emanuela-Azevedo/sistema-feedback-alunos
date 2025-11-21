package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import org.modelmapper.ModelMapper;

public class CursoMapper {
    private static final ModelMapper mapper = new ModelMapper();

    public static Curso toEntity(CursoCreateDTO dto){
        return mapper.map(dto, Curso.class);
    }
    public static CursoResponseDTO toDTO(Curso curso){
        return mapper.map(curso, CursoResponseDTO.class);
    }
}
