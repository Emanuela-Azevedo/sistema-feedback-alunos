package com.projetoDac.feedback_alunos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private CursoCreateDTO cursoCreateDTO;
    private CursoResponseDTO cursoResponseDTO;

    @BeforeEach
    void setUp() {
        cursoCreateDTO = new CursoCreateDTO();
        cursoCreateDTO.setNome("Engenharia de Software");

        cursoResponseDTO = new CursoResponseDTO();
        cursoResponseDTO.setIdCurso(1L);
        cursoResponseDTO.setNome("Engenharia de Software");
    }

    @Test
    void cadastrarCurso_DeveRetornar201() throws Exception {
        when(cursoService.cadastrarCurso(any(CursoCreateDTO.class))).thenReturn(cursoResponseDTO);

        mockMvc.perform(post("/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCurso").value(1L))
                .andExpect(jsonPath("$.nome").value("Engenharia de Software"));
    }

    @Test
    void listarCursos_DeveRetornar200() throws Exception {
        List<CursoResponseDTO> cursos = Arrays.asList(cursoResponseDTO);
        when(cursoService.listarCursos()).thenReturn(cursos);

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Engenharia de Software"));
    }
}