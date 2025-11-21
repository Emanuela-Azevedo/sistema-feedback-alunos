package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.UsuarioCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioResponseDTO;
import com.projetoDac.feedback_alunos.model.Usuario;
import org.modelmapper.ModelMapper;

public class UsuarioMapper {
	private static final ModelMapper mapper = new ModelMapper();

	public static Usuario toEntity(UsuarioCreateDTO dto) {
		return mapper.map(dto, Usuario.class);
	}

	public static UsuarioResponseDTO toDto(Usuario usuario) {
		return mapper.map(usuario, UsuarioResponseDTO.class);
	}

}
