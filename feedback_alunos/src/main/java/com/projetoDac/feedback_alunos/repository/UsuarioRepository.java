package com.projetoDac.feedback_alunos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoDac.feedback_alunos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByMatricula(String matricula);
    Optional<Usuario> findByEmail(String email);
    boolean existsByPerfisNomePerfil(String nomePerfil);
}