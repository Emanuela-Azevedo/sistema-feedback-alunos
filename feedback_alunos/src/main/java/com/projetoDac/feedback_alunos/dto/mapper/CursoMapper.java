package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Disciplina;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

public class CursoMapper {
    private static final ModelMapper mapper = new ModelMapper();

    public static Curso toEntity(CursoCreateDTO dto){
        return mapper.map(dto, Curso.class);
    }

    public static CursoResponseDTO toDTO(Curso curso){
        CursoResponseDTO dto = mapper.map(curso, CursoResponseDTO.class);

        if (curso.getDisciplinas() != null) {
            dto.setDisciplinas(
                    curso.getDisciplinas().stream()
                            .map(CursoMapper::mapDisciplinaToDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private static DisciplinaResponseDTO mapDisciplinaToDTO(Disciplina disciplina) {
        DisciplinaResponseDTO dto = new DisciplinaResponseDTO();
        dto.setIdDisciplina(disciplina.getIdDisciplina());
        dto.setNome(disciplina.getNome());

        if (disciplina.getProfessor() != null) {
            dto.setProfessorNome(disciplina.getProfessor().getNome());
        }

        return dto;
    }
}