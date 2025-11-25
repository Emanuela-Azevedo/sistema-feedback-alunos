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

import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AvaliacaoProfessorResponseDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

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
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    private String baseUrl;
    private Long alunoId;
    private Long professorId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/avaliacoes/professores";
        avaliacaoProfessorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();
        
        // Criar perfil aluno
        Perfil perfilAluno = new Perfil();
        perfilAluno.setNomePerfil("ALUNO");
        perfilAluno = perfilRepository.save(perfilAluno);
        
        // Criar perfil professor
        Perfil perfilProfessor = new Perfil();
        perfilProfessor.setNomePerfil("PROFESSOR");
        perfilProfessor = perfilRepository.save(perfilProfessor);
        
        // Criar usuário aluno
        Usuario aluno = new Usuario();
        aluno.setMatricula("ALU001");
        aluno.setNome("Aluno Teste");
        aluno.setSenha("senha123");
        aluno.getPerfis().add(perfilAluno);
        aluno = usuarioRepository.save(aluno);
        alunoId = aluno.getIdUsuario();
        
        // Criar usuário professor
        Usuario professor = new Usuario();
        professor.setMatricula("PROF001");
        professor.setNome("Professor Teste");
        professor.setSenha("senha123");
        professor.getPerfis().add(perfilProfessor);
        professor = usuarioRepository.save(professor);
        professorId = professor.getIdUsuario();
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
    void criarAvaliacaoProfessor_ComDadosValidos_DeveRetornar201() {
        AvaliacaoProfessorCreateDTO avaliacaoCreateDTO = new AvaliacaoProfessorCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(alunoId);
        avaliacaoCreateDTO.setProfessorId(professorId);
        avaliacaoCreateDTO.setNota(5);
        avaliacaoCreateDTO.setComentario("Excelente professor!");
        avaliacaoCreateDTO.setAnonima(false);

        ResponseEntity<AvaliacaoProfessorResponseDTO> response = restTemplate.postForEntity(
                baseUrl, avaliacaoCreateDTO, AvaliacaoProfessorResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getNota());
    }

    @Test
    void criarAvaliacaoProfessor_SemNota_DeveRetornar400() {
        AvaliacaoProfessorCreateDTO avaliacaoCreateDTO = new AvaliacaoProfessorCreateDTO();
        avaliacaoCreateDTO.setUsuarioId(alunoId);
        avaliacaoCreateDTO.setProfessorId(professorId);
        avaliacaoCreateDTO.setComentario("Bom professor");

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
    void excluirAvaliacao_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}