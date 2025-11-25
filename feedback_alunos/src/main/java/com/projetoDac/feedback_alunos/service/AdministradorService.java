package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AdministradorMapper;
import com.projetoDac.feedback_alunos.exception.AdministradorNaoEncontradoException;
import com.projetoDac.feedback_alunos.model.Administrador;
import com.projetoDac.feedback_alunos.model.Perfil;
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

	@Transactional
	public AdministratorResponseDTO cadastrarAdministrador(AdministradorCreateDTO dto) {
		// Verifica se já existe um administrador
		Optional<Administrador> existente = administradorRepository.findFirstByOrderByIdUsuarioAsc();
		if (existente.isPresent()) {
			return AdministradorMapper.toDTO(existente.get());
		}

		Administrador administrador = new Administrador();
		administrador.setNome(dto.getNome());
		administrador.setMatricula(dto.getMatricula());
		administrador.setSenha(dto.getSenha());
		administrador.setSuperAdmin(dto.isSuperAdmin());

		Optional<Perfil> perfilAdmin = perfilRepository.findByNomePerfil("ADMIN");
		perfilAdmin.ifPresent(administrador.getPerfis()::add);

		Administrador administradorSalvo = administradorRepository.save(administrador);
		return AdministradorMapper.toDTO(administradorSalvo);
	}

	
	public AdministratorResponseDTO buscarAdministrador() {
		Administrador admin = administradorRepository.findFirstByOrderByIdUsuarioAsc()
				.orElseThrow(AdministradorNaoEncontradoException::new);
		return AdministradorMapper.toDTO(admin);
	}

	@Transactional
	public AdministratorResponseDTO editarAdministrador(AdministradorCreateDTO dto) {
		Administrador administrador = administradorRepository.findFirstByOrderByIdUsuarioAsc()
				.orElseThrow(AdministradorNaoEncontradoException::new);

		administrador.setNome(dto.getNome());
		administrador.setMatricula(dto.getMatricula());
		administrador.setSuperAdmin(dto.isSuperAdmin());

		Administrador atualizado = administradorRepository.save(administrador);
		return AdministradorMapper.toDTO(atualizado);
	}
}
