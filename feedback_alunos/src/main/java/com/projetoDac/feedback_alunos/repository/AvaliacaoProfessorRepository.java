package com.projetoDac.feedback_alunos.repository;

import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoProfessorRepository extends JpaRepository<AvaliacaoProfessor, Long> {
}