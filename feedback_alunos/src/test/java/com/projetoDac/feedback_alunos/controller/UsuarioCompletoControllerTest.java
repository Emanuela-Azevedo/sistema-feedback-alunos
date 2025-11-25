package com.projetoDac.feedback_alunos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioCompletoController.class)
class UsuarioCompletoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioCompletoService usuarioCompletoService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioCompletoCreateDTO usuarioCreateDTO;
    private UsuarioCompletoResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("USR001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{1L, 2L});

        usuarioResponseDTO = new UsuarioCompletoResponseDTO();
        usuarioResponseDTO.setIdUsuario(1L);
        usuarioResponseDTO.setNome("João Silva");
        usuarioResponseDTO.setMatricula("USR001");
        usuarioResponseDTO.setCurso("Engenharia de Software");
        Set<String> perfis = new HashSet<>();
        perfis.add("ALUNO");
        perfis.add("PROFESSOR");
        usuarioResponseDTO.setPerfis(perfis);
    }

    @Test
    void criarUsuario_DeveRetornar201() throws Exception {
        when(usuarioCompletoService.criarUsuario(any(UsuarioCompletoCreateDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/usuarios-completo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void listarUsuarios_DeveRetornar200() throws Exception {
        List<UsuarioCompletoResponseDTO> usuarios = Arrays.asList(usuarioResponseDTO);
        when(usuarioCompletoService.listarUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios-completo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void buscarPorId_DeveRetornar200() throws Exception {
        when(usuarioCompletoService.buscarPorId(1L)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(get("/usuarios-completo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_ComIdInvalido_DeveRetornar404() throws Exception {
        when(usuarioCompletoService.buscarPorId(1L))
                .thenThrow(new UsuarioNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/usuarios-completo/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarUsuario_DeveRetornar200() throws Exception {
        when(usuarioCompletoService.atualizarUsuario(eq(1L), any(UsuarioCompletoCreateDTO.class)))
                .thenReturn(usuarioResponseDTO);

        mockMvc.perform(put("/usuarios-completo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void excluirUsuario_DeveRetornar204() throws Exception {
        mockMvc.perform(delete("/usuarios-completo/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluirUsuario_ComIdInvalido_DeveRetornar404() throws Exception {
        doThrow(new UsuarioNotFoundException("Usuário não encontrado"))
                .when(usuarioCompletoService).excluirUsuario(1L);

        mockMvc.perform(delete("/usuarios-completo/1"))
                .andExpect(status().isNotFound());
    }
}