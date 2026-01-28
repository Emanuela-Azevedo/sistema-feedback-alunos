package com.projetoDac.feedback_alunos.dto.mapper;

import java.util.stream.Collectors;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;

public class UsuarioCompletoMapper {

    public static Usuario toEntity(UsuarioCompletoCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setEmail(dto.getEmail());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());
        return usuario;
    }

    public static UsuarioCompletoResponseDTO toDTO(Usuario usuario) {
        UsuarioCompletoResponseDTO dto = new UsuarioCompletoResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setMatricula(usuario.getMatricula());
        dto.setEmail(usuario.getEmail());
        dto.setCurso(usuario.getCurso());
        dto.setEspecialidade(usuario.getEspecialidade());
        dto.setPerfis(usuario.getPerfis().stream()
                .map(Perfil::getNomePerfil)
                .collect(Collectors.toSet()));
        return dto;
    }
}