package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.model.Aluno;
import org.modelmapper.ModelMapper;

public class AlunoMapper {

    private static final ModelMapper mapper = new ModelMapper();

<<<<<<< Updated upstream
	public static Aluno toEntity(AlunoCreateDTO dto) {
		return mapper.map(dto, Aluno.class);
	}

	public static AlunoResponseDTO toDTO(Aluno aluno) {
		AlunoResponseDTO dto = mapper.map(aluno, AlunoResponseDTO.class);

		if (aluno.getUsuario() != null) {
			dto.setUsuarioId(aluno.getUsuario().getId());
		}

		return dto;
	}
=======
    // DTO → Entity
    public static Aluno toEntity(AlunoCreateDTO dto) {
        return mapper.map(dto, Aluno.class);
    }
>>>>>>> Stashed changes

    // Entity → DTO
    public static AlunoResponseDTO toDTO(Aluno aluno) {
        return mapper.map(aluno, AlunoResponseDTO.class);
    }
}
