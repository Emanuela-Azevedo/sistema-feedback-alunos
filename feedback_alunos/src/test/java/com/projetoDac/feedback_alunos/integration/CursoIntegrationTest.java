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

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.repository.CursoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CursoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CursoRepository cursoRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/cursos";
        cursoRepository.deleteAll();
    }

    @Test
    void listarCursos_DeveRetornarStatus200() {
        ResponseEntity<CursoResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, CursoResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cadastrarCurso_DeveRetornar201EPeristir() {
        CursoCreateDTO cursoCreateDTO = new CursoCreateDTO();
        cursoCreateDTO.setNome("Engenharia de Software");

        ResponseEntity<CursoResponseDTO> response = restTemplate.postForEntity(
                baseUrl, cursoCreateDTO, CursoResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Engenharia de Software", response.getBody().getNome());
        assertEquals(1, cursoRepository.count());
    }

    @Test
    void editarCurso_DeveRetornar200EAtualizar() {
        Curso curso = new Curso();
        curso.setNome("Curso Original");
        curso = cursoRepository.save(curso);

        CursoCreateDTO cursoCreateDTO = new CursoCreateDTO();
        cursoCreateDTO.setNome("Curso Atualizado");

        HttpEntity<CursoCreateDTO> request = new HttpEntity<>(cursoCreateDTO);
        ResponseEntity<CursoResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + curso.getIdCurso(), HttpMethod.PUT, request, CursoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Curso Atualizado", response.getBody().getNome());
    }

    @Test
    void excluirCurso_DeveRetornar204ERemover() {
        Curso curso = new Curso();
        curso.setNome("Curso para Excluir");
        curso = cursoRepository.save(curso);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + curso.getIdCurso(), HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, cursoRepository.count());
    }

    @Test
    void editarCurso_ComIdInexistente_DeveRetornar404() {
        CursoCreateDTO cursoCreateDTO = new CursoCreateDTO();
        cursoCreateDTO.setNome("Curso Inexistente");

        HttpEntity<CursoCreateDTO> request = new HttpEntity<>(cursoCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirCurso_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarCurso_ComIdValido_DeveRetornar200() {
        Curso curso = new Curso();
        curso.setNome("Curso Teste");
        curso = cursoRepository.save(curso);

        ResponseEntity<CursoResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + curso.getIdCurso(), CursoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Curso Teste", response.getBody().getNome());
    }

    @Test
    void buscarCurso_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarCurso_SemNome_DeveRetornar400() {
        CursoCreateDTO cursoCreateDTO = new CursoCreateDTO();

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, cursoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}