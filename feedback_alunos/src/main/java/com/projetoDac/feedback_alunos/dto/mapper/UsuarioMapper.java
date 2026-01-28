package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.model.Usuario;
import org.modelmapper.ModelMapper;

public class UsuarioMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Usuario toEntity(UsuarioCompletoResponseDTO dto){
        return mapper.map(dto, Usuario.class);
    }

    public static UsuarioCompletoResponseDTO toDTO(Usuario usuario){
        return mapper.map(usuario, UsuarioCompletoResponseDTO.class);
    }
}
