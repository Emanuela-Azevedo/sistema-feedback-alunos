package com.projetoDac.feedback_alunos.repository;

import com.projetoDac.feedback_alunos.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNomePerfil(String nomePerfil);
}