package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Disciplina;
import org.modelmapper.ModelMapper;

public class DisciplinaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Disciplina toEntity(DisciplinaCreateDTO dto){
        return mapper.map( dto, Disciplina.class );
    }
    public static DisciplinaResponseDTO toDTO(Disciplina disciplina){
        return mapper.map(disciplina, DisciplinaResponseDTO.class);
    }
}
