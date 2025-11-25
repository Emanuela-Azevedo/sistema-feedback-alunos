package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "matricula", nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "senha", nullable = false, length = 200)
    private String senha;

    @Column(name = "curso", length = 100)
    private String curso;

    @Column(name = "especialidade", length = 100)
    private String especialidade;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TB_usuario_perfil",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )
    private Set<Perfil> perfis = new HashSet<>();
}