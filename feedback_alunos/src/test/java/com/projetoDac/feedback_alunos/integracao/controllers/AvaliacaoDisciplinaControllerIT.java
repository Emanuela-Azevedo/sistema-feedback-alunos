package com.projetoDac.feedback_alunos.integracao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
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
class AvaliacaoDisciplinaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtAluno;
    private String jwtProfessor;
    private String jwtAdmin;
    private Disciplina disciplinaBase;

    @BeforeEach
    void setup() throws Exception {
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();
        disciplinaRepository.deleteAll();
        cursoRepository.deleteAll();

        Perfil perfilAluno = ensurePerfil("ROLE_ALUNO");
        Perfil perfilProfessor = ensurePerfil("ROLE_PROFESSOR");
        Perfil perfilAdmin = ensurePerfil("ROLE_ADMIN");

        Curso cursoBase = new Curso();
        cursoBase.setNome("Engenharia de Software");
        cursoRepository.save(cursoBase);
        System.out.println("=== CURSO CRIADO COM ID: " + cursoBase.getIdCurso() + " ===");

        Usuario professor = new Usuario();
        professor.setNome("Professor Teste");
        professor.setMatricula("prof123");
        professor.setSenha(passwordEncoder.encode("senhaProf"));
        professor.setCurso("Matemática");
        professor.setEspecialidade("Álgebra");
        professor.setPerfil(perfilProfessor);
        usuarioRepository.save(professor);

        disciplinaBase = new Disciplina();
        disciplinaBase.setNome("Matemática");
        disciplinaBase.setCurso(cursoBase);
        disciplinaBase.setProfessor(professor); // ✅ agora não fica null
        disciplinaRepository.save(disciplinaBase);

        System.out.println("=== DISCIPLINA CRIADA ===");
        System.out.println("ID: " + disciplinaBase.getIdDisciplina());
        System.out.println("Nome: " + disciplinaBase.getNome());
        System.out.println("Curso: " + disciplinaBase.getCurso().getNome());
        System.out.println("Professor: " + disciplinaBase.getProfessor().getNome());


        Usuario aluno = new Usuario();
        aluno.setNome("Aluno Teste");
        aluno.setMatricula("aluno123");
        aluno.setSenha(passwordEncoder.encode("senhaAluno"));
        aluno.setCurso("Engenharia");
        aluno.setEspecialidade("Software");
        aluno.setPerfil(perfilAluno);
        usuarioRepository.save(aluno);

        Usuario admin = new Usuario();
        admin.setNome("Admin Teste");
        admin.setMatricula("admin123");
        admin.setSenha(passwordEncoder.encode("senhaAdmin"));
        admin.setCurso("Gestão");
        admin.setEspecialidade("Administração");
        admin.setPerfil(perfilAdmin);
        usuarioRepository.save(admin);

        jwtAluno = loginComo("aluno123", "senhaAluno");
        jwtProfessor = loginComo("prof123", "senhaProf");
        jwtAdmin = loginComo("admin123", "senhaAdmin");

        System.out.println("=== TOKENS GERADOS ===");
        System.out.println("Aluno: " + jwtAluno);
        System.out.println("Professor: " + jwtProfessor);
        System.out.println("Admin: " + jwtAdmin);
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

        System.out.println("=== LOGIN RESPONSE PARA " + matricula + " === " + response);

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void criarAvaliacaoDisciplina_ComAlunoLogado_DeveRetornar201() throws Exception {
        Usuario aluno = usuarioRepository.findByMatricula("aluno123").orElseThrow();

        AvaliacaoDisciplinaCreateDTO dto = new AvaliacaoDisciplinaCreateDTO();
        dto.setUsuarioId(aluno.getIdUsuario());
        dto.setDisciplinaId(disciplinaBase.getIdDisciplina());
        dto.setNota(5);
        dto.setComentario("Ótima disciplina!");
        dto.setAnonima(false);

        var result = mockMvc.perform(post("/api/avaliacoes/disciplinas")
                        .header("Authorization", "Bearer " + jwtAluno)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void listarAvaliacoes_ComProfessorLogado_DeveRetornar200() throws Exception {
        var result = mockMvc.perform(get("/api/avaliacoes/disciplinas")
                        .header("Authorization", "Bearer " + jwtProfessor))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void buscarPorId_ComAdminLogado_DeveRetornar200() throws Exception {
        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setDisciplina(disciplinaBase);
        avaliacao.setComentario("Muito boa!");
        avaliacao.setNota(8);
        avaliacaoDisciplinaRepository.save(avaliacao);

        var result = mockMvc.perform(get("/api/avaliacoes/disciplinas/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAdmin))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void excluirAvaliacao_ComAdminLogado_DeveRetornar204() throws Exception {
        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setDisciplina(disciplinaBase);
        avaliacao.setComentario("Interessante");
        avaliacao.setNota(7);
        avaliacaoDisciplinaRepository.save(avaliacao);

        var result = mockMvc.perform(delete("/api/avaliacoes/disciplinas/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAdmin))
                .andReturn();
        assertEquals(204, result.getResponse().getStatus());
    }
    @Test
    void excluirAvaliacao_ComAlunoLogado_DeveRetornar204() throws Exception {
        Usuario aluno = usuarioRepository.findByMatricula("aluno123").orElseThrow();

        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setDisciplina(disciplinaBase);
        avaliacao.setComentario("Comentário inicial");
        avaliacao.setNota(4);
        avaliacao.setUsuario(aluno);
        avaliacaoDisciplinaRepository.save(avaliacao);

        var result = mockMvc.perform(delete("/api/avaliacoes/disciplinas/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAluno))
                .andReturn();
        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void editarAvaliacao_ComAlunoLogado_DeveRetornar200() throws Exception {
        Usuario aluno = usuarioRepository.findByMatricula("aluno123").orElseThrow();

        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setDisciplina(disciplinaBase);
        avaliacao.setComentario("Comentário inicial");
        avaliacao.setNota(3);
        avaliacao.setUsuario(aluno);
        avaliacaoDisciplinaRepository.save(avaliacao);

        AvaliacaoDisciplinaCreateDTO dto = new AvaliacaoDisciplinaCreateDTO();
        dto.setUsuarioId(aluno.getIdUsuario());
        dto.setDisciplinaId(disciplinaBase.getIdDisciplina());
        dto.setNota(5);
        dto.setComentario("Comentário atualizado");
        dto.setAnonima(false);

        var result = mockMvc.perform(put("/api/avaliacoes/disciplinas/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAluno)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
    @Test
    void criarAvaliacaoDisciplina_ComProfessorLogado_DeveRetornar403() throws Exception {

        Usuario professor = usuarioRepository.findByMatricula("prof123").orElseThrow();

        AvaliacaoDisciplinaCreateDTO dto = new AvaliacaoDisciplinaCreateDTO();
        dto.setUsuarioId(professor.getIdUsuario()); // mesmo que tente usar o próprio ID
        dto.setDisciplinaId(disciplinaBase.getIdDisciplina());
        dto.setNota(5);
        dto.setComentario("Tentativa indevida de avaliação");
        dto.setAnonima(false);

        var result = mockMvc.perform(post("/api/avaliacoes/disciplinas")
                        .header("Authorization", "Bearer " + jwtProfessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }
}