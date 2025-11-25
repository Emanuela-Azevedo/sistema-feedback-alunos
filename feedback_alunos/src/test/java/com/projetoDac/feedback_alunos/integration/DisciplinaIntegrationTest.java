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

import com.projetoDac.feedback_alunos.dto.DisciplinaCreateDTO;
import com.projetoDac.feedback_alunos.dto.DisciplinaResponseDTO;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

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
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    private String baseUrl;
    private Long cursoId;
    private Long professorId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/disciplinas";
        avaliacaoDisciplinaRepository.deleteAll();
        avaliacaoProfessorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        usuarioRepository.deleteAll();
        cursoRepository.deleteAll();
        perfilRepository.deleteAll();
        
        // Criar perfil professor
        Perfil perfilProfessor = new Perfil();
        perfilProfessor.setNomePerfil("PROFESSOR");
        perfilProfessor = perfilRepository.save(perfilProfessor);
        
        // Criar curso
        Curso curso = new Curso();
        curso.setNome("Curso Teste");
        curso = cursoRepository.save(curso);
        cursoId = curso.getIdCurso();
        
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
    void cadastrarDisciplina_ComDadosValidos_DeveRetornar201() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setNome("Programação Java");
        disciplinaCreateDTO.setCursoId(cursoId);
        disciplinaCreateDTO.setProfessorId(professorId);

        ResponseEntity<DisciplinaResponseDTO> response = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, DisciplinaResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Programação Java", response.getBody().getNome());
    }

    @Test
    void cadastrarDisciplina_SemNome_DeveRetornar400() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setCursoId(cursoId);
        disciplinaCreateDTO.setProfessorId(professorId);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void editarDisciplina_ComDadosValidos_DeveRetornar200() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setNome("Programação Java");
        disciplinaCreateDTO.setCursoId(cursoId);
        disciplinaCreateDTO.setProfessorId(professorId);

        ResponseEntity<DisciplinaResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, DisciplinaResponseDTO.class);
        Long disciplinaId = createResponse.getBody().getIdDisciplina();

        disciplinaCreateDTO.setNome("Programação Java Avançada");

        HttpEntity<DisciplinaCreateDTO> request = new HttpEntity<>(disciplinaCreateDTO);
        ResponseEntity<DisciplinaResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + disciplinaId, HttpMethod.PUT, request, DisciplinaResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Programação Java Avançada", response.getBody().getNome());
    }

    @Test
    void editarDisciplina_ComIdInexistente_DeveRetornar404() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setNome("Disciplina Inexistente");
        disciplinaCreateDTO.setCursoId(cursoId);
        disciplinaCreateDTO.setProfessorId(professorId);

        HttpEntity<DisciplinaCreateDTO> request = new HttpEntity<>(disciplinaCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirDisciplina_ComIdValido_DeveRetornar204() {
        DisciplinaCreateDTO disciplinaCreateDTO = new DisciplinaCreateDTO();
        disciplinaCreateDTO.setNome("Programação Java");
        disciplinaCreateDTO.setCursoId(cursoId);
        disciplinaCreateDTO.setProfessorId(professorId);

        ResponseEntity<DisciplinaResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, disciplinaCreateDTO, DisciplinaResponseDTO.class);
        Long disciplinaId = createResponse.getBody().getIdDisciplina();

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + disciplinaId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void excluirDisciplina_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}