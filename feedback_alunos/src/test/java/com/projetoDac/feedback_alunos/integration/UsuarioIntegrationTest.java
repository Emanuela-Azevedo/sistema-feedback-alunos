package com.projetoDac.feedback_alunos.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
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

import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsuarioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/usuarios";
        usuarioRepository.deleteAll();
    }

    @Test
    void listarUsuarios_DeveRetornarStatus200() {
        ResponseEntity<UsuarioCompletoResponseDTO[]> response = restTemplate.getForEntity(
                baseUrl, UsuarioCompletoResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cadastrarUsuario_DeveRetornar201EPersistir() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("USR001");
        usuarioCreateDTO.setSenha("senha123");

        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals("USR001", response.getBody().getMatricula());
        assertEquals(1, usuarioRepository.count());
    }

    @Test
    void buscarUsuarioPorId_DeveRetornar200() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setMatricula("USER001");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + usuario.getIdUsuario(), UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

    @Test
    void editarUsuario_DeveRetornar200EAtualizar() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Original");
        usuario.setMatricula("USER002");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        UsuarioCompletoCreateDTO usuarioCreateCompletoDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateCompletoDTO.setNome("Usuario Atualizado");
        usuarioCreateCompletoDTO.setMatricula("USER002UPD");
        usuarioCreateCompletoDTO.setSenha("novaSenha");

        HttpEntity<UsuarioCompletoCreateDTO> request = new HttpEntity<>(usuarioCreateCompletoDTO);
        ResponseEntity<UsuarioCompletoResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + usuario.getIdUsuario(), HttpMethod.PUT, request, UsuarioCompletoResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario Atualizado", response.getBody().getNome());
    }

    @Test
    void excluirUsuario_DeveRetornar204ERemover() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario para Excluir");
        usuario.setMatricula("USER003");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + usuario.getIdUsuario(), HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, usuarioRepository.count());
    }

    @Test
    void editarUsuario_ComIdInexistente_DeveRetornar404() {
        UsuarioCompletoCreateDTO usuarioCompletoCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCompletoCreateDTO.setNome("Usuario Inexistente");
        usuarioCompletoCreateDTO.setMatricula("INEXIST001");
        usuarioCompletoCreateDTO.setSenha("senha123");

        HttpEntity<UsuarioCompletoCreateDTO> request = new HttpEntity<>(usuarioCompletoCreateDTO);
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
    void buscarUsuarioPorId_ComIdInexistente_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarUsuario_ComDadosInvalidos_DeveRetornar400() {
        UsuarioCompletoCreateDTO usuarioCreateDTO = new UsuarioCompletoCreateDTO();
        usuarioCreateDTO.setNome(""); // Nome vazio
        usuarioCreateDTO.setMatricula(""); // Matrícula vazia
        usuarioCreateDTO.setSenha("123"); // Senha muito curta

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, usuarioCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}