package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import org.modelmapper.ModelMapper;

public class PerfilMapper {
	private static final ModelMapper mapper = new ModelMapper();

	public static Perfil toEntity(PerfilCreateDTO dto) {
		return mapper.map(dto, Perfil.class);
	}

	public static PerfilResponseDTO toDTO(Perfil perfil) {
		return mapper.map(perfil, PerfilResponseDTO.class);
	}

}
