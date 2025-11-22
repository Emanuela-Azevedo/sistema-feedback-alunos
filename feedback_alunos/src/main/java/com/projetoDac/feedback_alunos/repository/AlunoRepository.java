package com.projetoDac.feedback_alunos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoDac.feedback_alunos.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByMatricula(String matricula);
}
