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

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AlunoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/alunos";
        usuarioRepository.deleteAll();
    }

    @Test
    void listarAlunos_DeveRetornarStatus200() {
        ResponseEntity<AlunoResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, AlunoResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cadastrarAluno_SemUsuario_DeveRetornar500() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome("João Silva");
        alunoCreateDTO.setMatricula("ALU001");
        alunoCreateDTO.setCurso("Engenharia");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, alunoCreateDTO, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void editarAluno_ComIdInexistente_DeveRetornar404() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome("Aluno Inexistente");
        alunoCreateDTO.setMatricula("ALU999");
        alunoCreateDTO.setCurso("Curso Inexistente");

        HttpEntity<AlunoCreateDTO> request = new HttpEntity<>(alunoCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirAluno_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarAlunoPorId_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarAluno_ComDadosInvalidos_DeveRetornar400() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome(""); // Nome vazio
        alunoCreateDTO.setMatricula(""); // Matrícula vazia
        alunoCreateDTO.setCurso(""); // Curso vazio

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, alunoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}