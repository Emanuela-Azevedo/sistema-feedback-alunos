package com.projetoDac.feedback_alunos.integracao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AvaliacaoProfessorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtAluno;
    private String jwtAdmin;

    private Usuario aluno;
    private Usuario professor;

    @BeforeEach
    void setup() throws Exception {
        avaliacaoProfessorRepository.deleteAll();
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();

        Perfil perfilAluno = ensurePerfil("ROLE_ALUNO");
        Perfil perfilAdmin = ensurePerfil("ROLE_ADMIN");
        Perfil perfilProfessor = ensurePerfil("ROLE_PROFESSOR");

        aluno = new Usuario();
        aluno.setNome("Aluno Teste");
        aluno.setMatricula("aluno123");
        aluno.setSenha(passwordEncoder.encode("senhaAluno"));
        aluno.setCurso("Engenharia");
        aluno.setEspecialidade("Software");
        aluno.setPerfil(perfilAluno);
        usuarioRepository.save(aluno);

        professor = new Usuario();
        professor.setNome("Professor Teste");
        professor.setMatricula("prof123");
        professor.setSenha(passwordEncoder.encode("senhaProf"));
        professor.setCurso("Matemática");
        professor.setEspecialidade("Álgebra");
        professor.setPerfil(perfilProfessor);
        usuarioRepository.save(professor);

        Usuario admin = new Usuario();
        admin.setNome("Admin Teste");
        admin.setMatricula("admin123");
        admin.setSenha(passwordEncoder.encode("senhaAdmin"));
        admin.setCurso("Gestão");
        admin.setEspecialidade("Administração");
        admin.setPerfil(perfilAdmin);
        usuarioRepository.save(admin);

        jwtAluno = loginComo("aluno123", "senhaAluno");
        jwtAdmin = loginComo("admin123", "senhaAdmin");
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
                        .content(objectMapper.writeValueAsString(
                                new UsuarioLoginDTO(matricula, senha))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void criarAvaliacaoProfessor_ComAlunoLogado_DeveRetornar201() throws Exception {
        AvaliacaoProfessorCreateDTO dto = new AvaliacaoProfessorCreateDTO();
        dto.setUsuarioId(aluno.getIdUsuario());
        dto.setProfessorId(professor.getIdUsuario());
        dto.setNota(5);
        dto.setComentario("Ótimo professor!");
        dto.setAnonima(false);

        var result = mockMvc.perform(post("/avaliacoes/professores")
                        .header("Authorization", "Bearer " + jwtAluno)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void editarAvaliacaoProfessor_ComAlunoAutor_DeveRetornar200() throws Exception {


        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(aluno);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(3);
        avaliacao.setComentario("Comentário inicial");
        avaliacaoProfessorRepository.save(avaliacao);


        AvaliacaoProfessorCreateDTO dto = new AvaliacaoProfessorCreateDTO();
        dto.setUsuarioId(aluno.getIdUsuario());
        dto.setProfessorId(professor.getIdUsuario());
        dto.setNota(5);
        dto.setComentario("Comentário atualizado");
        dto.setAnonima(false);

        var result = mockMvc.perform(
                        put("/avaliacoes/professores/" + avaliacao.getId())
                                .header("Authorization", "Bearer " + jwtAluno)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString()
                .contains("Comentário atualizado"));
    }


    @Test
    void editarAvaliacaoProfessor_ComAlunoNaoAutor_DeveRetornar403() throws Exception {
        Usuario outroAluno = new Usuario();
        outroAluno.setNome("Outro Aluno");
        outroAluno.setMatricula("aluno456");
        outroAluno.setSenha(passwordEncoder.encode("senhaOutro"));
        outroAluno.setCurso("Engenharia");
        outroAluno.setEspecialidade("Software");
        outroAluno.setPerfil(ensurePerfil("ROLE_ALUNO"));
        usuarioRepository.save(outroAluno);

        String jwtOutroAluno = loginComo("aluno456", "senhaOutro");

        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(aluno);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(3);
        avaliacao.setComentario("Comentário inicial");
        avaliacaoProfessorRepository.save(avaliacao);

        AvaliacaoProfessorCreateDTO dto = new AvaliacaoProfessorCreateDTO();
        dto.setUsuarioId(outroAluno.getIdUsuario());
        dto.setProfessorId(professor.getIdUsuario());
        dto.setNota(5);
        dto.setComentario("Tentativa inválida");
        dto.setAnonima(false);

        var result = mockMvc.perform(put("/avaliacoes/professores/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtOutroAluno)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    void excluirAvaliacaoProfessor_ComAlunoAutor_DeveRetornar204() throws Exception {
        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(aluno);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(4);
        avaliacao.setComentario("Comentário para excluir");
        avaliacaoProfessorRepository.save(avaliacao);

        var result = mockMvc.perform(delete("/avaliacoes/professores/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAluno))
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void excluirAvaliacaoProfessor_ComAlunoNaoAutor_DeveRetornar403() throws Exception {
        Usuario outroAluno = new Usuario();
        outroAluno.setNome("Outro Aluno");
        outroAluno.setMatricula("aluno456");
        outroAluno.setSenha(passwordEncoder.encode("senhaOutro"));
        outroAluno.setCurso("Engenharia");
        outroAluno.setEspecialidade("Software");
        outroAluno.setPerfil(ensurePerfil("ROLE_ALUNO"));
        usuarioRepository.save(outroAluno);

        String jwtOutroAluno = loginComo("aluno456", "senhaOutro");

        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(aluno);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(4);
        avaliacao.setComentario("Comentário protegido");
        avaliacaoProfessorRepository.save(avaliacao);

        var result = mockMvc.perform(delete("/avaliacoes/professores/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtOutroAluno))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    void excluirAvaliacaoProfessor_ComAdminLogado_DeveRetornar204() throws Exception {
        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(aluno);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(4);
        avaliacao.setComentario("Comentário excluído pelo admin");
        avaliacaoProfessorRepository.save(avaliacao);

        var result = mockMvc.perform(delete("/avaliacoes/professores/" + avaliacao.getId())
                        .header("Authorization", "Bearer " + jwtAdmin))
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
        assertTrue(avaliacaoProfessorRepository
                .findById(avaliacao.getId()).isEmpty());
    }
}