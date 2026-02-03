package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.model.Usuario;

import java.util.Set;

public class UsuarioCompletoMapper {

    public static Usuario toEntity(UsuarioCompletoCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());
        return usuario;
    }

    public static UsuarioCompletoResponseDTO toDTO(Usuario usuario) {
        UsuarioCompletoResponseDTO dto = new UsuarioCompletoResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setMatricula(usuario.getMatricula());
        dto.setCurso(usuario.getCurso());
        dto.setEspecialidade(usuario.getEspecialidade());

        if (usuario.getPerfil() != null) {
            dto.setPerfis(Set.of(usuario.getPerfil().getNomePerfil()));
        } else {
            dto.setPerfis(Set.of());
        }

        return dto;
    }
}