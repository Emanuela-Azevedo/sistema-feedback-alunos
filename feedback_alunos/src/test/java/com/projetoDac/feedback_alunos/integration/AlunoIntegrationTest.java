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
    void cadastrarAluno_SemSenha_DeveRetornar400() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome("João Silva");
        alunoCreateDTO.setMatricula("ALU001");
        alunoCreateDTO.setCurso("Engenharia");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, alunoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void editarAluno_ComMatriculaInexistente_DeveRetornar404() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome("Aluno Inexistente");
        alunoCreateDTO.setMatricula("ALU999");
        alunoCreateDTO.setCurso("Curso Inexistente");
        alunoCreateDTO.setSenha("123456");

        HttpEntity<AlunoCreateDTO> request = new HttpEntity<>(alunoCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/matricula/ALU999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirAluno_ComMatriculaInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/matricula/ALU999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarAlunoPorMatricula_ComMatriculaInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/matricula/MAT999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarAluno_ComDadosInvalidos_DeveRetornar400() {
        AlunoCreateDTO alunoCreateDTO = new AlunoCreateDTO();
        alunoCreateDTO.setNome("");
        alunoCreateDTO.setMatricula("");
        alunoCreateDTO.setCurso("");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, alunoCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}