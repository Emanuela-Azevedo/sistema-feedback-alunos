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
import org.springframework.test.context.ActiveProfiles;

import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoDisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.AvaliacaoDisciplina;
import com.projetoDac.feedback_alunos.model.Disciplina;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AvaliacaoDisciplinaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/avaliacoes/disciplinas";
        avaliacaoDisciplinaRepository.deleteAll();
        disciplinaRepository.deleteAll();
        usuarioRepository.deleteAll();
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
        usuario = usuarioRepository.save(usuario);

        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Disciplina Teste");
        disciplina = disciplinaRepository.save(disciplina);

        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setUsuario(usuario);
        avaliacao.setDisciplina(disciplina);
        avaliacao.setNota(4);
        avaliacao.setComentario("Boa disciplina");
        avaliacao.setAnonima(true);
        avaliacao = avaliacaoDisciplinaRepository.save(avaliacao);

        ResponseEntity<AvaliacaoDisciplinaResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + avaliacao.getId(), AvaliacaoDisciplinaResponseDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getNota());
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
        usuario = usuarioRepository.save(usuario);

        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Disciplina Teste");
        disciplina = disciplinaRepository.save(disciplina);

        AvaliacaoDisciplina avaliacao = new AvaliacaoDisciplina();
        avaliacao.setUsuario(usuario);
        avaliacao.setDisciplina(disciplina);
        avaliacao.setNota(4);
        avaliacao.setComentario("Boa disciplina");
        avaliacao.setAnonima(true);
        avaliacao = avaliacaoDisciplinaRepository.save(avaliacao);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + avaliacao.getId(), HttpMethod.DELETE, null, Void.class);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(avaliacaoDisciplinaRepository.existsById(avaliacao.getId()));
    }
}