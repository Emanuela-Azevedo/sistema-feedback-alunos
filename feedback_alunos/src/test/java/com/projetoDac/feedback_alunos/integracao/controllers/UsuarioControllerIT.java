package com.projetoDac.feedback_alunos.integracao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;

    @BeforeEach
    void setup() throws Exception {
        // limpa o banco antes de cada teste
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();

        // garante que os três perfis existam
        Perfil perfilAdmin = ensurePerfil("ROLE_ADMIN");
        Perfil perfilProfessor = ensurePerfil("ROLE_PROFESSOR");
        Perfil perfilAluno = ensurePerfil("ROLE_ALUNO");

        // cria um admin inicial para autenticação
        Usuario admin = new Usuario();
        admin.setNome("Administrador Teste");
        admin.setMatricula("admin123");
        admin.setSenha(passwordEncoder.encode("senha123"));
        admin.setCurso("Gestão");
        admin.setEspecialidade("Administração");
        admin.setPerfis(List.of(perfilAdmin));

        usuarioRepository.save(admin);

        // login para capturar token
        jwtToken = loginComo("admin123", "senha123");

        System.out.println("=== TOKEN GERADO NO SETUP ===");
        System.out.println(jwtToken);

        System.out.println("=== PERFIS DO USUÁRIO ADMIN ===");
        usuarioRepository.findByMatricula("admin123")
                .ifPresent(u -> u.getPerfis()
                        .forEach(p -> System.out.println("Perfil: " + p.getNomePerfil())));
    }

    private Perfil ensurePerfil(String nomePerfil) {
        return perfilRepository.findByNomePerfil(nomePerfil)
                .orElseGet(() -> {
                    Perfil novo = new Perfil();
                    novo.setNomePerfil(nomePerfil);
                    return perfilRepository.save(novo);
                });
    }

    private String loginComo(String matricula, String senha) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UsuarioLoginDTO(matricula, senha))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void listarUsuarios_ComAdminLogado_DeveRetornarLista() throws Exception {
        var result = mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Bearer " + jwtToken))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void criarAluno_ComAdminLogado_DeveRetornar201() throws Exception {
        Perfil perfilAluno = perfilRepository.findByNomePerfil("ROLE_ALUNO").orElseThrow();

        UsuarioCompletoCreateDTO dto = new UsuarioCompletoCreateDTO();
        dto.setNome("Aluno Teste");
        dto.setMatricula("aluno123");
        dto.setSenha("senhaAluno");
        dto.setCurso("Engenharia");
        dto.setEspecialidade("Software");
        dto.setPerfilIds(new Long[]{perfilAluno.getId()});

        var result = mockMvc.perform(post("/usuarios/aluno")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void criarProfessor_ComAdminLogado_DeveRetornar201() throws Exception {
        Perfil perfilProfessor = perfilRepository.findByNomePerfil("ROLE_PROFESSOR").orElseThrow();

        UsuarioCompletoCreateDTO dto = new UsuarioCompletoCreateDTO();
        dto.setNome("Professor Teste");
        dto.setMatricula("prof123");
        dto.setSenha("senhaProf");
        dto.setCurso("Matemática");
        dto.setEspecialidade("Álgebra");
        dto.setPerfilIds(new Long[]{perfilProfessor.getId()});

        var result = mockMvc.perform(post("/usuarios/professor")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void criarPrimeiroAdmin_SemLogin_DeveRetornar201() throws Exception {
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();
        System.out.println("=== BANCO LIMPO ===");
        System.out.println("Perfis existentes: " + perfilRepository.findAll());
        System.out.println("Usuários existentes: " + usuarioRepository.findAll());

        Perfil perfilAdmin = ensurePerfil("ROLE_ADMIN");
        System.out.println("=== PERFIL ADMIN GARANTIDO COM ID: " + perfilAdmin.getId() + " ===");

        UsuarioCompletoCreateDTO dto = new UsuarioCompletoCreateDTO();
        dto.setNome("Primeiro Admin");
        dto.setMatricula("primeiroAdmin");
        dto.setSenha("senhaAdmin");
        dto.setCurso("Gestão");
        dto.setEspecialidade("Administração");
        dto.setPerfilIds(new Long[]{perfilAdmin.getId()});

        var result = mockMvc.perform(post("/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void criarOutroAdmin_QuandoJaExiste_DeveRetornarErro() throws Exception {
        Perfil perfilAdmin = perfilRepository.findByNomePerfil("ROLE_ADMIN").orElseThrow();

        UsuarioCompletoCreateDTO dto = new UsuarioCompletoCreateDTO();
        dto.setNome("Segundo Admin");
        dto.setMatricula("segundoAdmin");
        dto.setSenha("senhaAdmin2");
        dto.setCurso("Gestão");
        dto.setEspecialidade("Administração");
        dto.setPerfilIds(new Long[]{perfilAdmin.getId()});

        var result = mockMvc.perform(post("/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void excluirUsuario_ComAdminLogado_DeveRetornar204() throws Exception {
        Usuario usuario = usuarioRepository.findAll().get(0);

        var result = mockMvc.perform(delete("/usuarios/" + usuario.getIdUsuario())
                        .header("Authorization", "Bearer " + jwtToken))
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void listarUsuarios_SemEstarLogado_DeveRetornarErro() throws Exception {
        var result = mockMvc.perform(get("/usuarios")) // sem Authorization
                .andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }
}