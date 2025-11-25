package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.model.Administrador;
import org.modelmapper.ModelMapper;

public class AdministradorMapper {

	private static final ModelMapper mapper = new ModelMapper();

	public static Administrador toEntity(AdministradorCreateDTO dto) {
		return mapper.map(dto, Administrador.class);
	}

	public static AdministratorResponseDTO toDTO(Administrador administrador) {
		AdministratorResponseDTO dto = mapper.map(administrador, AdministratorResponseDTO.class);
		
		if (administrador.getUsuario() != null) {
			dto.setIdUsuario(administrador.getUsuario().getIdUsuario());
		}
		
		return dto;
	}
}