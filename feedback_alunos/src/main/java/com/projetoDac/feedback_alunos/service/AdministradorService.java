package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AdministradorMapper;
import com.projetoDac.feedback_alunos.exception.AdministradorNaoEncontradoException;
import com.projetoDac.feedback_alunos.model.Administrador;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import com.projetoDac.feedback_alunos.repository.AdministradorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdministradorService {

	@Autowired
	private AdministradorRepository administradorRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	/**
	 * Cadastrar administrador único (singleton)
	 */
	@Transactional
	public AdministratorResponseDTO cadastrarAdministrador(AdministradorCreateDTO dto) {
		// Verifica se já existe um administrador
		Optional<Administrador> existente = administradorRepository.findFirstByOrderByIdAsc();
		if (existente.isPresent()) {
			return AdministradorMapper.toDTO(existente.get());
		}

		// Criar Usuario
		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setMatricula(dto.getMatricula());
		usuario.setSenha(dto.getSenha());

		// Atribuir perfil ADMIN
		Optional<Perfil> perfilAdmin = perfilRepository.findByNomePerfil("ADMIN");
		perfilAdmin.ifPresent(usuario.getPerfis()::add);

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// Criar Administrador
		Administrador administrador = Administrador.getInstance();
		administrador.setNome(dto.getNome());
		administrador.setMatricula(dto.getMatricula());
		administrador.setSuperAdmin(dto.isSuperAdmin());

		Administrador administradorSalvo = administradorRepository.save(administrador);
		return AdministradorMapper.toDTO(administradorSalvo);
	}

	/**
	 * Buscar administrador único
	 */
	public AdministratorResponseDTO buscarAdministrador() {
		Administrador admin = administradorRepository.findFirstByOrderByIdAsc()
				.orElseThrow(AdministradorNaoEncontradoException::new);
		return AdministradorMapper.toDTO(admin);
	}

	/**
	 * Editar administrador
	 */
	@Transactional
	public AdministratorResponseDTO editarAdministrador(AdministradorCreateDTO dto) {
		Administrador administrador = administradorRepository.findFirstByOrderByIdAsc()
				.orElseThrow(AdministradorNaoEncontradoException::new);

		administrador.setNome(dto.getNome());
		administrador.setMatricula(dto.getMatricula());
		administrador.setSuperAdmin(dto.isSuperAdmin());

		Administrador atualizado = administradorRepository.save(administrador);
		return AdministradorMapper.toDTO(atualizado);
	}

	/**
	 * Excluir administrador (opcional, apenas se permitido)
	 */
	public void excluirAdministrador() {
		administradorRepository.findFirstByOrderByIdAsc().ifPresent(administradorRepository::delete);
	}
}
