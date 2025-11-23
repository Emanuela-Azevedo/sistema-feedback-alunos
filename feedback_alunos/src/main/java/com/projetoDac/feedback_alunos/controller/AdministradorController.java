package com.projetoDac.feedback_alunos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

	@PostMapping
	public ResponseEntity<?> cadastrarAdministrador(
			@Valid @RequestBody AdministradorCreateDTO dto, BindingResult result) {
		
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body("Dados inválidos");
		}

		AdministratorResponseDTO administrador = administradorService.cadastrarAdministrador(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(administrador);
	}

	@GetMapping
	public ResponseEntity<AdministratorResponseDTO> buscarAdministrador() {
		AdministratorResponseDTO administrador = administradorService.buscarAdministrador();
		return ResponseEntity.ok(administrador);
	}

	@PutMapping
	public ResponseEntity<?> editarAdministrador(
			@Valid @RequestBody AdministradorCreateDTO dto, BindingResult result) {
		
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body("Dados inválidos");
		}

		AdministratorResponseDTO administrador = administradorService.editarAdministrador(dto);
		return ResponseEntity.ok(administrador);
	}
}