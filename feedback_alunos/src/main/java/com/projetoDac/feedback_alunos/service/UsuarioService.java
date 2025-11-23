package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.UsuarioCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.UsuarioMapper;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO usuarioCreateDTO) {
		Usuario usuario = UsuarioMapper.toEntity(usuarioCreateDTO);
		Usuario salvo = usuarioRepository.save(usuario);
		return UsuarioMapper.toDTO(salvo);
	}

	public UsuarioResponseDTO editarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {

		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

		usuario.setNome(usuarioCreateDTO.getNome());
		usuario.setMatricula(usuarioCreateDTO.getMatricula());
		usuario.setSenha(usuarioCreateDTO.getSenha());

		Usuario atualizado = usuarioRepository.save(usuario);
		return UsuarioMapper.toDTO(atualizado);
	}

	public void excluirUsuario(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new UsuarioNotFoundException("Usuário não encontrado com ID: " + id);
		}
		usuarioRepository.deleteById(id);
	}

	public List<UsuarioResponseDTO> listarUsuarios() {
		return usuarioRepository.findAll().stream().map(UsuarioMapper::toDTO).toList();
	}

	public UsuarioResponseDTO buscarPorId(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

		return UsuarioMapper.toDTO(usuario);
	}
}
