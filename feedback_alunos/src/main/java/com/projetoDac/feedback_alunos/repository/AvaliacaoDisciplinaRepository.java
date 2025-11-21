package com.projetoDac.feedback_alunos.repository;

import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoDisciplinaRepository extends JpaRepository<AvaliacaoDisciplina, Long> {
}