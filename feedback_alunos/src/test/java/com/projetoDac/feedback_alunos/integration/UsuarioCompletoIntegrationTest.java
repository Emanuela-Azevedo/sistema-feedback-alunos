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

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsuarioCompletoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    private String baseUrl;
    private Long perfilAlunoId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/usuarios-completo";
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();
        
        // Criar perfil ALUNO para os testes
        Perfil perfilAluno = new Perfil();
        perfilAluno.setNomePerfil("ALUNO");
        perfilAluno = perfilRepository.save(perfilAluno);
        perfilAlunoId = perfilAluno.getIdPerfil();
    }

    @Test
    void criarUsuario_DeveRetornar201EPersistir() throws Exception {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("USR001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals("USR001", response.getBody().getMatricula());
        assertEquals("Engenharia de Software", response.getBody().getCurso());
    }

    @Test
    void listarUsuarios_DeveRetornarStatus200() {
        ResponseEntity<UsuarioCompletoResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, UsuarioCompletoResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void atualizarUsuario_ComIdInexistente_DeveRetornar404() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("Usuario Inexistente");
        usuarioCreateDTO.setMatricula("INEXIST001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        HttpEntity<UsuarioCompletoCreateDTO> request = new HttpEntity<>(usuarioCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void excluirUsuario_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void criarUsuario_ComDadosInvalidos_DeveRetornar400() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome(""); // Nome vazio
        usuarioCreateDTO.setMatricula(""); // Matrícula vazia
        usuarioCreateDTO.setSenha("123"); // Senha muito curta

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void buscarPorMatricula_ComMatriculaValida_DeveRetornar200() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("ALU001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        restTemplate.postForEntity(baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);

        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/matricula/ALU001", UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ALU001", response.getBody().getMatricula());
    }

    @Test
    void buscarPorMatricula_ComMatriculaInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/matricula/INEXISTENTE", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void atualizarUsuario_ComDadosValidos_DeveRetornar200() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("ALU001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        ResponseEntity<UsuarioCompletoResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);
        Long usuarioId = createResponse.getBody().getIdUsuario();

        usuarioCreateDTO.setNome("João Silva Atualizado");
        usuarioCreateDTO.setCurso("Ciência da Computação");

        HttpEntity<UsuarioCompletoCreateDTO> request = new HttpEntity<>(usuarioCreateDTO);
        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + usuarioId, HttpMethod.PUT, request, UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João Silva Atualizado", response.getBody().getNome());
        assertEquals("Ciência da Computação", response.getBody().getCurso());
    }

    @Test
    void excluirUsuario_ComIdValido_DeveRetornar204() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("ALU001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        ResponseEntity<UsuarioCompletoResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);
        Long usuarioId = createResponse.getBody().getIdUsuario();

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + usuarioId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void buscarPorId_ComUsuarioExistente_DeveRetornar200() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("ALU001");
        usuarioCreateDTO.setSenha("senha123");
        usuarioCreateDTO.setCurso("Engenharia de Software");
        usuarioCreateDTO.setPerfilIds(new Long[]{perfilAlunoId});

        ResponseEntity<UsuarioCompletoResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);
        Long usuarioId = createResponse.getBody().getIdUsuario();

        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + usuarioId, UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }
}