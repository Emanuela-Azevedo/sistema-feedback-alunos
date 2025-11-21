package com.projetoDac.feedback_alunos.repository;

import com.projetoDac.feedback_alunos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}