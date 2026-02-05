package com.projetoDac.feedback_alunos.dto.mapper;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoUpdateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Curso;

import java.util.List;

public class UsuarioCompletoMapper {

    public static Usuario toEntity(UsuarioCompletoCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());

        if (dto.getCursoId() != null) {
            Curso curso = new Curso();
            curso.setIdCurso(dto.getCursoId());
            usuario.setCurso(curso);
        }

        usuario.setEspecialidade(dto.getEspecialidade());
        return usuario;
    }


    // Atualização parcial
    public static Usuario toEntityUpdate(UsuarioCompletoUpdateDTO dto, Usuario usuarioExistente) {
        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            usuarioExistente.setNome(dto.getNome());
        }
        if (dto.getEspecialidade() != null && !dto.getEspecialidade().isBlank()) {
            usuarioExistente.setEspecialidade(dto.getEspecialidade());
        }
        if (dto.getPerfil() != null && !dto.getPerfil().isBlank()) {
            Perfil perfil = new Perfil();
            perfil.setNomePerfil(dto.getPerfil());
            usuarioExistente.setPerfil(perfil);
        }
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuarioExistente.setSenha(dto.getSenha()); // aplicar encode se usar Spring Security
        }
        if (dto.getCursoId() != null) {
            Curso curso = new Curso();
            curso.setIdCurso(dto.getCursoId());
            usuarioExistente.setCurso(curso);
        }
        if (dto.getDisciplinas() != null && !dto.getDisciplinas().isEmpty()) {
            List<Disciplina> disciplinas = dto.getDisciplinas().stream()
                    .map(id -> {
                        Disciplina d = new Disciplina();
                        d.setIdDisciplina(id);
                        return d;
                    })
                    .toList();
            usuarioExistente.setDisciplinas(disciplinas);
        }
        return usuarioExistente;
    }

    // Response DTO
    public static UsuarioCompletoResponseDTO toDTO(Usuario usuario) {
        UsuarioCompletoResponseDTO dto = new UsuarioCompletoResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setMatricula(usuario.getMatricula());
        dto.setEspecialidade(usuario.getEspecialidade());
        dto.setPerfil(usuario.getPerfil() != null ? usuario.getPerfil().getNomePerfil() : null);

        // Curso → retorna apenas o nome no UsuarioCompletoResponseDTO
        dto.setCurso(usuario.getCurso() != null ? usuario.getCurso().getNome() : null);

        dto.setDisciplinas(usuario.getDisciplinas() != null
                ? usuario.getDisciplinas().stream().map(Disciplina::getNome).toList()
                : null);

        dto.setSuperAdmin(false); // ajuste conforme sua regra de negócio
        return dto;
    }

    // Novo: Mapper para CursoResponseDTO
    public static CursoResponseDTO toCursoDTO(Curso curso) {
        if (curso == null) return null;
        return new CursoResponseDTO(curso.getIdCurso(), curso.getNome());
    }
}