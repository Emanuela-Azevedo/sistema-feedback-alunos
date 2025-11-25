package com.projetoDac.feedback_alunos.dto.mapper;

import org.modelmapper.ModelMapper;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Disciplina;

public class DisciplinaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Disciplina toEntity(DisciplinaCreateDTO dto){
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.getNome());
        return disciplina;
    }
    public static DisciplinaResponseDTO toDTO(Disciplina disciplina){
        DisciplinaResponseDTO dto = mapper.map(disciplina, DisciplinaResponseDTO.class);
        if (disciplina.getCurso() != null) {
            dto.setCursoId(disciplina.getCurso().getIdCurso());
        }
        if (disciplina.getProfessor() != null) {
            dto.setProfessorId(disciplina.getProfessor().getIdUsuario());
        }
        return dto;
    }
}
