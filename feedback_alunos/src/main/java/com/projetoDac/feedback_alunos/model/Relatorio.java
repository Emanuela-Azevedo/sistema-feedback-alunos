package com.projetoDac.feedback_alunos.model;

import com.projetoDac.feedback_alunos.model.enums.TipoAvaliacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_relatorio")
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private TipoAvaliacao tipo;

    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    @Lob
    @Column(nullable = false)
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "gerado_por", nullable = false)
    private Usuario geradoPor;
}
