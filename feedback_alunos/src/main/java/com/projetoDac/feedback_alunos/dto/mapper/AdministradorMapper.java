package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.model.Administrador;
import org.modelmapper.ModelMapper;

public class AdministradorMapper {

	private static final ModelMapper mapper = new ModelMapper();

	// DTO → Entity
	public static Administrador toEntity(AdministradorCreateDTO dto) {
		return mapper.map(dto, Administrador.class);
	}

	// Entity → DTO
	public static AdministratorResponseDTO toDTO(Administrador administrador) {
		return mapper.map(administrador, AdministratorResponseDTO.class);
	}
}