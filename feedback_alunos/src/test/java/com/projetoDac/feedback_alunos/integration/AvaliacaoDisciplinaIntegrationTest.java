package com.projetoDac.feedback_alunos.integration;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
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
class AvaliacaoDisciplinaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/avaliacoes/disciplinas";
        avaliacaoDisciplinaRepository.deleteAll();
    }

    @Test
    void criarAvaliacaoDisciplina_ComIdInvalido_DeveRetornar404() {
        AvaliacaoDisciplinaCreateDTO avaliacaoCreateDTO = new AvaliacaoDisciplinaCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(999L);
        avaliacaoCreateDTO.setDisciplinaId(999L);
        avaliacaoCreateDTO.setNota(5);
        avaliacaoCreateDTO.setComentario("Excelente disciplina!");
        avaliacaoCreateDTO.setAnonima(false);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, avaliacaoCreateDTO, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void criarAvaliacaoDisciplina_SemNota_DeveRetornar400() {
        AvaliacaoDisciplinaCreateDTO avaliacaoCreateDTO = new AvaliacaoDisciplinaCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(1L);
        avaliacaoCreateDTO.setDisciplinaId(1L);
        avaliacaoCreateDTO.setComentario("Bom");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, avaliacaoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}