package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
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
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_perfil")
    )
    private List<Perfil> perfis = new ArrayList<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPerfis();
    }

    public String getPassword() {
        return senha;
    }

    public String getUsername() {
        return matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario user))
            return false;
        return Objects.equals(idUsuario, user.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUsuario);
    }
}