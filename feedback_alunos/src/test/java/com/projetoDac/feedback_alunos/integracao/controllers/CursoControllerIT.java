package com.projetoDac.feedback_alunos.integracao.controllers;

import com.projetoDac.feedback_alunos.FeedbackAlunosApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.repository.CursoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FeedbackAlunosApplication.class)
@AutoConfigureMockMvc
@Transactional
class CursoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void setup() {
        cursoRepository.deleteAll();
    }

    // ==========================
    // CADASTRAR CURSO (ADMIN)

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrarCurso_ComDadosValidos_DeveRetornar201() throws Exception {

    CursoCreateDTO dto = new CursoCreateDTO();
    dto.setNome("Engenharia de Software");

    mockMvc.perform(post("/cursos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idCurso").exists())
            .andExpect(jsonPath("$.nome").value("Engenharia de Software"));
    }
     
    // ==========================
    // EDITAR CURSO (ADMIN)

    @Test
    @WithMockUser(roles = "ADMIN")
    void editarCurso_ComIdExistente_DeveRetornar200() throws Exception {

        Curso curso = new Curso();
        curso.setNome("ADS");
        curso = cursoRepository.save(curso);

        CursoCreateDTO dto = new CursoCreateDTO();
        dto.setNome("Análise e Desenvolvimento de Sistemas");

       mockMvc.perform(put("/cursos/{id}", curso.getIdCurso())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome")
                .value("Análise e Desenvolvimento de Sistemas"));

    }

    // ==========================
    // EXCLUIR CURSO (ADMIN)

    @Test
    @WithMockUser(roles = "ADMIN")
    void excluirCurso_ComIdExistente_DeveRetornar204() throws Exception {

        Curso curso = new Curso();
        curso.setNome("Curso Teste");
        curso = cursoRepository.save(curso);

        mockMvc.perform(delete("/cursos/{id}", curso.getIdCurso()))
        .andExpect(status().isNoContent());

    }


    // ==========================
    // LISTAR CURSOS (ADMIN ou ALUNO)

    @Test
    @WithMockUser(roles = "ALUNO")
    void listarCursos_DeveRetornarLista() throws Exception {

        Curso curso = new Curso();
        curso.setNome("Direito");
        cursoRepository.save(curso);

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Direito"));
    }

    // ==========================
    // BUSCAR CURSO POR ID (ADMIN ou ALUNO)

    @Test
    @WithMockUser(roles = "ALUNO")
    void buscarCursoPorId_ComIdExistente_DeveRetornar200() throws Exception {

        Curso curso = new Curso();
        curso.setNome("Medicina");
        curso = cursoRepository.save(curso);

       mockMvc.perform(get("/cursos/{id}", curso.getIdCurso()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Medicina"));

    }

    // ==========================
    // ACESSO NEGADO (SEM ROLE)

    @Test
    @WithMockUser(roles = "ALUNO")
    void cadastrarCurso_ComUsuarioNaoAdmin_DeveRetornar403() throws Exception {

        CursoCreateDTO dto = new CursoCreateDTO();
        dto.setNome("Curso Proibido");

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}