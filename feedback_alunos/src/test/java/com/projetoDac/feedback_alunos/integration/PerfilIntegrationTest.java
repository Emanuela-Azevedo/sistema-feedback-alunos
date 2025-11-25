package com.projetoDac.feedback_alunos.integration;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.AvaliacaoDisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.AvaliacaoProfessorRepository;
import com.projetoDac.feedback_alunos.repository.DisciplinaRepository;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PerfilIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private AvaliacaoDisciplinaRepository avaliacaoDisciplinaRepository;

    @Autowired
    private AvaliacaoProfessorRepository avaliacaoProfessorRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/perfis";
        avaliacaoDisciplinaRepository.deleteAll();
        avaliacaoProfessorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();
    }

    @Test
    void cadastrarPerfil_DeveRetornar201EPeristir() {
        PerfilCreateDTO perfilCreateDTO = new PerfilCreateDTO();
        perfilCreateDTO.setNome("ADMIN");

        ResponseEntity<PerfilResponseDTO> response = restTemplate.postForEntity(
                baseUrl, perfilCreateDTO, PerfilResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADMIN", response.getBody().getNome());
        assertEquals(1, perfilRepository.count());
    }

    @Test
    void listarPerfis_DeveRetornarStatus200() {
        ResponseEntity<PerfilResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, PerfilResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarPorId_ComIdValido_DeveRetornar200() {
        Perfil perfil = new Perfil();
        perfil.setNomePerfil("ALUNO");
        perfil = perfilRepository.save(perfil);

        ResponseEntity<PerfilResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + perfil.getIdPerfil(), PerfilResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ALUNO", response.getBody().getNome());
    }

    @Test
    void buscarPorId_ComIdInvalido_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarPerfil_SemNome_DeveRetornar400() {
        PerfilCreateDTO perfilCreateDTO = new PerfilCreateDTO();

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, perfilCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void listarPerfis_ComDados_DeveRetornarLista() {
        Perfil perfil1 = new Perfil();
        perfil1.setNomePerfil("ADMIN");
        perfilRepository.save(perfil1);

        Perfil perfil2 = new Perfil();
        perfil2.setNomePerfil("ALUNO");
        perfilRepository.save(perfil2);

        ResponseEntity<PerfilResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, PerfilResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void editarPerfil_ComDadosValidos_DeveRetornar200() {
        Perfil perfil = new Perfil();
        perfil.setNomePerfil("ALUNO");
        perfil = perfilRepository.save(perfil);

        PerfilCreateDTO perfilCreateDTO = new PerfilCreateDTO();
        perfilCreateDTO.setNome("ESTUDANTE");

        HttpEntity<PerfilCreateDTO> request = new HttpEntity<>(perfilCreateDTO);
        ResponseEntity<PerfilResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + perfil.getIdPerfil(), HttpMethod.PUT, request, PerfilResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ESTUDANTE", response.getBody().getNome());
    }

    @Test
    void editarPerfil_ComIdInexistente_DeveRetornar404() {
        PerfilCreateDTO perfilCreateDTO = new PerfilCreateDTO();
        perfilCreateDTO.setNome("INEXISTENTE");

        HttpEntity<PerfilCreateDTO> request = new HttpEntity<>(perfilCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirPerfil_ComIdValido_DeveRetornar204() {
        Perfil perfil = new Perfil();
        perfil.setNomePerfil("TEMPORARIO");
        perfil = perfilRepository.save(perfil);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + perfil.getIdPerfil(), HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void excluirPerfil_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}