package com.projetoDac.feedback_alunos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoDac.feedback_alunos.model.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

	// Retorna a primeira instância de administrador (para garantir singleton)
	Optional<Administrador> findFirstByOrderByIdUsuarioAsc();
}
