package com.projetoDac.feedback_alunos.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@Service
public class UsuarioCompletoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional
    public UsuarioCompletoResponseDTO criarUsuario(UsuarioCompletoCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());

        Set<Perfil> perfis = new HashSet<>();
        for (Long perfilId : dto.getPerfilIds()) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));
            perfis.add(perfil);
        }
        usuario.setPerfis(perfis);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toResponseDTO(usuarioSalvo);
    }

    public List<UsuarioCompletoResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioCompletoResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioCompletoResponseDTO atualizarUsuario(Long id, UsuarioCompletoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());

        Set<Perfil> perfis = new HashSet<>();
        for (Long perfilId : dto.getPerfilIds()) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));
            perfis.add(perfil);
        }
        usuario.setPerfis(perfis);

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return toResponseDTO(usuarioAtualizado);
    }

    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioCompletoResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioCompletoResponseDTO dto = new UsuarioCompletoResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setMatricula(usuario.getMatricula());
        dto.setCurso(usuario.getCurso());
        dto.setEspecialidade(usuario.getEspecialidade());
        dto.setPerfis(usuario.getPerfis().stream()
                .map(Perfil::getNomePerfil)
                .collect(Collectors.toSet()));
        return dto;
    }
}