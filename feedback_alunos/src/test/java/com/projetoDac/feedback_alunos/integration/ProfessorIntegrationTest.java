package com.projetoDac.feedback_alunos.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.projetoDac.feedback_alunos.dto.ProfessorCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProfessorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/professores";
        professorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void listarProfessores_DeveRetornarStatus200() {
        ResponseEntity<ProfessorResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, ProfessorResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cadastrarProfessor_DeveRetornar201EPeristir() {
        ProfessorCompletoCreateDTO professorCreateDTO = new ProfessorCompletoCreateDTO();
        professorCreateDTO.setNome("João Silva");
        professorCreateDTO.setMatricula("PROF001");
        professorCreateDTO.setSenha("senha123");
        professorCreateDTO.setEspecialidade("Matemática");

        ResponseEntity<ProfessorResponseDTO> response = restTemplate.postForEntity(
                baseUrl, professorCreateDTO, ProfessorResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals("PROF001", response.getBody().getMatricula());
        assertEquals("Matemática", response.getBody().getEspecialidade());
    }

    @Test
    void editarProfessor_ComIdInexistente_DeveRetornar404() {
        ProfessorCompletoCreateDTO professorCreateDTO = new ProfessorCompletoCreateDTO();
        professorCreateDTO.setNome("Professor Inexistente");
        professorCreateDTO.setMatricula("PROF999");
        professorCreateDTO.setSenha("senha123");
        professorCreateDTO.setEspecialidade("Inexistente");

        HttpEntity<ProfessorCompletoCreateDTO> request = new HttpEntity<>(professorCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirProfessor_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarProfessorPorMatricula_DeveRetornar200() {
        ProfessorCompletoCreateDTO professorCreateDTO = new ProfessorCompletoCreateDTO();
        professorCreateDTO.setNome("Maria Silva");
        professorCreateDTO.setMatricula("PROF002");
        professorCreateDTO.setSenha("senha123");
        professorCreateDTO.setEspecialidade("Física");

        restTemplate.postForEntity(baseUrl, professorCreateDTO, ProfessorResponseDTO.class);

        ResponseEntity<ProfessorResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/matricula/PROF002", ProfessorResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria Silva", response.getBody().getNome());
        assertEquals("PROF002", response.getBody().getMatricula());
    }

    @Test
    void buscarProfessorPorMatricula_ComMatriculaInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/matricula/PROF999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}