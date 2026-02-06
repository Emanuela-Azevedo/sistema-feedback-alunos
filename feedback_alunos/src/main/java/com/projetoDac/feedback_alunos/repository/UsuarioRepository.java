package com.projetoDac.feedback_alunos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projetoDac.feedback_alunos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByMatricula(String matricula);

    boolean existsByPerfilNomePerfil(String nomePerfil);

    List<Usuario> findByPerfil_NomePerfil(String nomePerfil);

    @Query("SELECT u FROM Usuario u WHERE u.curso.idCurso = :idCurso AND u.perfil.nomePerfil = 'ROLE_PROFESSOR'")
    List<Usuario> findProfessoresByCursoId(@Param("idCurso") Long idCurso);

}
