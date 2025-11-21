package com.projetoDac.feedback_alunos.integration;

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
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
class DisciplinaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/disciplinas";
        disciplinaRepository.deleteAll();
        professorRepository.deleteAll();
        usuarioRepository.deleteAll();
        cursoRepository.deleteAll();
    }

    @Test
    void listarDisciplinas_DeveRetornarStatus200() {
        ResponseEntity<DisciplinaResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, DisciplinaResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cadastrarDisciplina_ComIdInvalido_DeveRetornar404() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setNome("Programação Java");
        disciplinaCreateDTO.setCursoId(999L);
        disciplinaCreateDTO.setProfessorId(999L);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarDisciplina_SemNome_DeveRetornar400() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setCursoId(1L);
        disciplinaCreateDTO.setProfessorId(1L);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}