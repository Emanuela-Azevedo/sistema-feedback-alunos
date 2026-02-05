package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Disciplina;

public class DisciplinaMapper {

    public static Disciplina toEntity(DisciplinaCreateDTO dto) {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.getNome());
        return disciplina;
    }

    public static DisciplinaResponseDTO toDTO(Disciplina disciplina) {
        DisciplinaResponseDTO dto = new DisciplinaResponseDTO();
        dto.setIdDisciplina(disciplina.getIdDisciplina());
        dto.setNome(disciplina.getNome());

        if (disciplina.getCurso() != null) {
            dto.setCursoId(disciplina.getCurso().getIdCurso());
            dto.setCursoNome(disciplina.getCurso().getNome());
        }

        if (disciplina.getProfessor() != null) {
            dto.setProfessorId(disciplina.getProfessor().getIdUsuario());
            dto.setProfessorNome(disciplina.getProfessor().getNome());
        }

        return dto;
    }
}