package com.projetoDac.feedback_alunos.integracao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DisciplinaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Curso curso;
    private Usuario professor;

    @BeforeEach
    void setup() {
        disciplinaRepository.deleteAll();
        usuarioRepository.deleteAll();
        cursoRepository.deleteAll();

        // Cria curso
        curso = new Curso();
        curso.setNome("Curso de Teste");
        curso = cursoRepository.save(curso);

        // Cria usuário que representa o professor
        professor = new Usuario();
        professor.setNome("Professor Teste");
        professor.setMatricula("prof123");
        professor.setSenha("123456");
        professor.setCurso("ADS");
        professor.setEspecialidade("Programação");

        professor = usuarioRepository.save(professor);
    }

    // ===============================
    // CADASTRAR DISCIPLINA
    // ===============================
    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrarDisciplina_ComDadosValidos_DeveRetornar201() throws Exception {

        DisciplinaCreateDTO dto = new DisciplinaCreateDTO(
                "Algoritmos",
                curso.getIdCurso(),
                professor.getIdUsuario()
        );

        mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDisciplina").exists())
                .andExpect(jsonPath("$.nome").value("Algoritmos"));
    }

    // ===============================
    // EXCLUIR DISCIPLINA
    // ===============================
    @Test
    @WithMockUser(roles = "ADMIN")
    void excluirDisciplina_DeveRetornar204() throws Exception {

        DisciplinaCreateDTO dto = new DisciplinaCreateDTO(
                "Banco de Dados",
                curso.getIdCurso(),
                professor.getIdUsuario()
        );

        String response = mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("idDisciplina").asLong();

        mockMvc.perform(delete("/disciplinas/{id}", id))
                .andExpect(status().isNoContent());
    }

  // ===============================
    // EDITAR
    // ===============================
    @Test
    @WithMockUser(roles = "ADMIN")
    void editarDisciplina_ComDadosValidos_DeveRetornar200() throws Exception {

        String response = mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DisciplinaCreateDTO(
                                        "BD",
                                        curso.getIdCurso(),
                                        professor.getIdUsuario()
                                ))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long idDisciplina = objectMapper.readTree(response).get("idDisciplina").asLong();

        DisciplinaCreateDTO dtoAtualizado = new DisciplinaCreateDTO(
                "Banco de Dados",
                curso.getIdCurso(),
                professor.getIdUsuario()
        );

        mockMvc.perform(put("/disciplinas/{id}", idDisciplina)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Banco de Dados"));
    }

    // ===============================
    // BUSCAR POR ID
    // ===============================
    @Test
    @WithMockUser(roles = {"ADMIN", "ALUNO"})
    void buscarDisciplinaPorId_DeveRetornar200() throws Exception {

        String response = mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DisciplinaCreateDTO(
                                        "Engenharia de Software",
                                        curso.getIdCurso(),
                                        professor.getIdUsuario()
                                ))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long idDisciplina = objectMapper.readTree(response).get("idDisciplina").asLong();

        mockMvc.perform(get("/disciplinas/{id}", idDisciplina))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Engenharia de Software"));
    }

    // ===============================
    // LISTAR
    // ===============================
    @Test
    @WithMockUser(roles = {"ADMIN", "ALUNO"})
    void listarDisciplinas_DeveRetornar200() throws Exception {

        mockMvc.perform(get("/disciplinas"))
                .andExpect(status().isOk());
    }


}
