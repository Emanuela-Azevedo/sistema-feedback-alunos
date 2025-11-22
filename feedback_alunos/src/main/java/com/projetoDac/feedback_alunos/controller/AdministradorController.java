package com.projetoDac.feedback_alunos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.service.AdministradorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;

	// Cadastrar administrador único
	@PostMapping
	public ResponseEntity<AdministratorResponseDTO> cadastrarAdministrador(
			@Valid @RequestBody AdministradorCreateDTO dto) {

		AdministratorResponseDTO administrador = administradorService.cadastrarAdministrador(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(administrador);
	}

	// Buscar administrador único
	@GetMapping
	public ResponseEntity<AdministratorResponseDTO> buscarAdministrador() {
		AdministratorResponseDTO administrador = administradorService.buscarAdministrador();
		return ResponseEntity.ok(administrador);
	}

	// Editar administrador
	@PutMapping
	public ResponseEntity<AdministratorResponseDTO> editarAdministrador(
			@Valid @RequestBody AdministradorCreateDTO dto) {

		AdministratorResponseDTO administrador = administradorService.editarAdministrador(dto);
		return ResponseEntity.ok(administrador);
	}

	// Excluir administrador (opcional)
	@DeleteMapping
	public ResponseEntity<Void> excluirAdministrador() {
		administradorService.excluirAdministrador();
		return ResponseEntity.noContent().build();
	}
}