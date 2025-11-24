package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.ProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.Professor;
import org.modelmapper.ModelMapper;

public class ProfessorMapper {

	 private static final ModelMapper mapper = new ModelMapper();

	    public static Professor toEntity(ProfessorCreateDTO dto) {
	        return mapper.map(dto, Professor.class);
	    }

	    public static ProfessorResponseDTO toDTO(Professor professor) {
	        return mapper.map(professor, ProfessorResponseDTO.class);
	        
	    }
}