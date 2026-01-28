package com.projetoDac.feedback_alunos.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.UsuarioCompletoMapper;
import com.projetoDac.feedback_alunos.exception.AdminJaExisteException;
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
        if (dto.getPerfilIds() == null || dto.getPerfilIds().length == 0) {
            throw new PerfilNotFoundException("Pelo menos um perfil deve ser selecionado");
        }
        
        Usuario usuario = UsuarioCompletoMapper.toEntity(dto);

        Set<Perfil> perfis = new HashSet<>();
        boolean isAdmin = false;
        
        for (Long perfilId : dto.getPerfilIds()) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));
            
            if ("ADMIN".equals(perfil.getNomePerfil())) {
                isAdmin = true;
                if (usuarioRepository.existsByPerfisNomePerfil("ADMIN")) {
                    throw new AdminJaExisteException("Já existe um administrador cadastrado no sistema. Apenas um administrador é permitido.");
                }
            }
            
            perfis.add(perfil);
        }
        usuario.setPerfis(new ArrayList<>(perfis));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioCompletoMapper.toDTO(usuarioSalvo);
    }

    public List<UsuarioCompletoResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioCompletoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioCompletoResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
        return UsuarioCompletoMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioCompletoResponseDTO atualizarUsuario(Long id, UsuarioCompletoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setEmail(dto.getEmail());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());

        Set<Perfil> perfis = new HashSet<>();
        for (Long perfilId : dto.getPerfilIds()) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));
            perfis.add(perfil);
        }
        usuario.setPerfis(new ArrayList<>(perfis));

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioCompletoMapper.toDTO(usuarioAtualizado);
    }

    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioCompletoResponseDTO buscarPorMatricula(String matricula) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com matrícula: " + matricula));
        return UsuarioCompletoMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioCompletoResponseDTO criarAluno(UsuarioCompletoCreateDTO dto) {
        Perfil perfilAluno = perfilRepository.findByNomePerfil("ALUNO")
                .orElseThrow(() -> new PerfilNotFoundException("Perfil ALUNO não encontrado"));
        
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        usuario.setEmail(dto.getEmail());
        usuario.setCurso(dto.getCurso());
        usuario.setEspecialidade(dto.getEspecialidade());
        
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilAluno);
        usuario.setPerfis(perfis);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioCompletoMapper.toDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioCompletoResponseDTO criarProfessor(UsuarioCompletoCreateDTO dto) {
        Perfil perfilProfessor = perfilRepository.findByNomePerfil("PROFESSOR")
                .orElseThrow(() -> new PerfilNotFoundException("Perfil PROFESSOR não encontrado"));
        
        Usuario usuario = UsuarioCompletoMapper.toEntity(dto);
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilProfessor);
        usuario.setPerfis(perfis);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioCompletoMapper.toDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioCompletoResponseDTO criarAdmin(UsuarioCompletoCreateDTO dto) {
        if (usuarioRepository.existsByPerfisNomePerfil("ADMIN")) {
            throw new AdminJaExisteException("Já existe um administrador cadastrado no sistema. Apenas um administrador é permitido.");
        }
        
        Perfil perfilAdmin = perfilRepository.findByNomePerfil("ADMIN")
                .orElseThrow(() -> new PerfilNotFoundException("Perfil ADMIN não encontrado"));
        
        Usuario usuario = UsuarioCompletoMapper.toEntity(dto);
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilAdmin);
        usuario.setPerfis(perfis);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioCompletoMapper.toDTO(usuarioSalvo);
    }
}