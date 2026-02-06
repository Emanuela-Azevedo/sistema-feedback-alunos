package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.exception.AdminJaExisteException;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioCompletoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Usuario save(Usuario usuario, String perfilNome) {
        Perfil perfil = perfilRepository.findByNomePerfil(perfilNome)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado: " + perfilNome));

        usuario.setPerfil(perfil);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (!"ROLE_ADMIN".equals(perfil.getNomePerfil())) {
            if (usuario.getCurso() != null && usuario.getCurso().getIdCurso() != null) {
                Curso curso = cursoRepository.findById(usuario.getCurso().getIdCurso())
                        .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
                usuario.setCurso(curso);
            }
            usuario.setEspecialidade(usuario.getEspecialidade());
        } else {
            usuario.setCurso(null);
            usuario.setEspecialidade(null);
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado, String perfilNome) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setMatricula(usuarioAtualizado.getMatricula());
        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        usuario.setCurso(usuarioAtualizado.getCurso());
        usuario.setEspecialidade(usuarioAtualizado.getEspecialidade());

        if (perfilNome != null && !perfilNome.isBlank()) {
            Perfil perfil = perfilRepository.findByNomePerfil(perfilNome)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado: " + perfilNome));
            usuario.setPerfil(perfil);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorMatricula(String matricula) {
        return usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com matrícula: " + matricula));
    }

    public List<Usuario> listarUsuariosPorPerfil(String perfilNome) {
        return usuarioRepository.findByPerfil_NomePerfil(perfilNome);
    }


    public List<Usuario> listarProfessoresPorCurso(Long cursoId) {
        return usuarioRepository.findProfessoresByCursoId(cursoId); }
}