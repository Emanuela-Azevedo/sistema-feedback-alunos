package com.projetoDac.feedback_alunos.integration;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AvaliacaoProfessorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/avaliacoes/professores";
        avaliacaoProfessorRepository.deleteAll();
        professorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void criarAvaliacaoProfessor_ComIdInvalido_DeveRetornar404() {
        AvaliacaoProfessorCreateDTO avaliacaoCreateDTO = new AvaliacaoProfessorCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(999L);
        avaliacaoCreateDTO.setProfessorId(999L);
        avaliacaoCreateDTO.setNota(5);
        avaliacaoCreateDTO.setComentario("Excelente professor!");
        avaliacaoCreateDTO.setAnonima(false);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, avaliacaoCreateDTO, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void criarAvaliacaoProfessor_SemNota_DeveRetornar400() {
        AvaliacaoProfessorCreateDTO avaliacaoCreateDTO = new AvaliacaoProfessorCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(1L);
        avaliacaoCreateDTO.setProfessorId(1L);
        avaliacaoCreateDTO.setComentario("Bom professor");
        avaliacaoCreateDTO.setAnonima(false);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, avaliacaoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}