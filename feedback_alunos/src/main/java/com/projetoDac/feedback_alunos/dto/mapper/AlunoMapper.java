package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.model.Aluno;
import org.modelmapper.ModelMapper;

public class AlunoMapper {

	private static final ModelMapper mapper = new ModelMapper();

	public static Aluno toEntity(AlunoCreateDTO dto) {
		return mapper.map(dto, Aluno.class);
	}

	public static AlunoResponseDTO toDTO(Aluno aluno) {
		AlunoResponseDTO dto = mapper.map(aluno, AlunoResponseDTO.class);

		if (aluno.getUsuario() != null) {
			dto.setIdAluno(aluno.getUsuario().getIdUsuario());
			
		}

		return dto;
	}

}
