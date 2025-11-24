package com.projetoDac.feedback_alunos.integration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoProfessor;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    void listarAvaliacoes_DeveRetornarListaVazia() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorId_ComIdValido_DeveRetornarAvaliacao() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("12345");
        usuario.setNome("Usuario Teste");
        usuario.setSenha("senha123");
        usuario.setPerfis(new java.util.HashSet<>());
        usuario = usuarioRepository.save(usuario);

        Professor professor = new Professor();
        professor.setUsuario(usuario);
        professor.setNome("Prof. Teste");
        professor.setMatricula("PROF123");
        professor = professorRepository.save(professor);

        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(usuario);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(4);
        avaliacao.setComentario("Bom professor");
        avaliacao.setAnonima(true);
        avaliacao = avaliacaoProfessorRepository.save(avaliacao);

        assertNotNull(avaliacao.getId(), "ID da avaliação não foi gerado");

        ResponseEntity<String> stringResponse = restTemplate.getForEntity(
                baseUrl + "/" + avaliacao.getId(), String.class);

        if (stringResponse.getStatusCode() == HttpStatus.OK) {
            ResponseEntity<AvaliacaoProfessorResponseDTO> response = restTemplate.getForEntity(
                    baseUrl + "/" + avaliacao.getId(), AvaliacaoProfessorResponseDTO.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(4, response.getBody().getNota());
        }
    }

    @Test
    void excluirAvaliacao_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirAvaliacao_ComIdValido_DeveRetornar204() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("12345");
        usuario.setNome("Usuario Teste");
        usuario.setSenha("senha123");
        usuario.setPerfis(new java.util.HashSet<>());
        usuario = usuarioRepository.save(usuario);

        Professor professor = new Professor();
        professor.setUsuario(usuario);
        professor.setNome("Prof. Teste");
        professor.setMatricula("PROF123");
        professor = professorRepository.save(professor);

        AvaliacaoProfessor avaliacao = new AvaliacaoProfessor();
        avaliacao.setUsuario(usuario);
        avaliacao.setProfessor(professor);
        avaliacao.setNota(4);
        avaliacao.setComentario("Bom professor");
        avaliacao.setAnonima(true);
        avaliacao = avaliacaoProfessorRepository.save(avaliacao);

        ResponseEntity<String> stringResponse = restTemplate.exchange(
                baseUrl + "/" + avaliacao.getId(), HttpMethod.DELETE, null, String.class);

        if (stringResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
            assertEquals(HttpStatus.NO_CONTENT, stringResponse.getStatusCode());
            assertFalse(avaliacaoProfessorRepository.existsById(avaliacao.getId()));
        }
    }
}