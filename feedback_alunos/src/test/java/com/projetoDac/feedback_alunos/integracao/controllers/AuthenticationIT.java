package com.projetoDac.feedback_alunos.unitarios.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoDac.feedback_alunos.dto.UsuarioLoginDTO;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @BeforeEach
    void setup() {
        avaliacaoDisciplinaRepository.deleteAll();// primeiro remove dependências
        avaliacaoProfessorRepository.deleteAll();
        usuarioRepository.deleteAll();             // depois remove usuários

        Usuario usuario = new Usuario();
        usuario.setNome("Usuário de Teste");
        usuario.setMatricula("usuarioTeste");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setCurso("Curso de Teste");
        usuario.setEspecialidade("Especialidade Teste");

        usuarioRepository.save(usuario);
    }



    @Test
    void login_ComCredenciaisValidas_DeveRetornarToken() throws Exception {
        UsuarioLoginDTO dto = new UsuarioLoginDTO("usuarioTeste", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_ComCredenciaisInvalidas_DeveRetornarErro400() throws Exception {
        UsuarioLoginDTO dto = new UsuarioLoginDTO("usuarioInvalido", "senhaErrada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }

    @Test
    void logout_DeveRetornarMensagemSucesso() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout realizado com sucesso!"));
    }
}