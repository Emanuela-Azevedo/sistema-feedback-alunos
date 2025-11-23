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

import com.projetoDac.feedback_alunos.dto.AdministradorCreateDTO;
import com.projetoDac.feedback_alunos.dto.AdministratorResponseDTO;
import com.projetoDac.feedback_alunos.repository.AdministradorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdministradorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/administradores";
        administradorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void buscarAdministrador_SemAdministrador_DeveRetornar404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarAdministrador_ComDadosInvalidos_DeveRetornar400() {
        AdministradorCreateDTO adminCreateDTO = new AdministradorCreateDTO();
        adminCreateDTO.setNome("");
        adminCreateDTO.setMatricula("");
        adminCreateDTO.setSenha("");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, adminCreateDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void cadastrarAdministrador_ComDadosValidos_DeveRetornar201() {
        AdministradorCreateDTO adminCreateDTO = new AdministradorCreateDTO();
        adminCreateDTO.setNome("Admin Silva");
        adminCreateDTO.setMatricula("ADM001");
        adminCreateDTO.setSenha("123456");
        adminCreateDTO.setSuperAdmin(false);

        ResponseEntity<AdministratorResponseDTO> response = restTemplate.postForEntity(
                baseUrl, adminCreateDTO, AdministratorResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void editarAdministrador_SemAdministrador_DeveRetornar404() {
        AdministradorCreateDTO adminCreateDTO = new AdministradorCreateDTO();
        adminCreateDTO.setNome("Admin Editado");
        adminCreateDTO.setMatricula("ADM002");
        adminCreateDTO.setSenha("123456");
        adminCreateDTO.setSuperAdmin(true);

        HttpEntity<AdministradorCreateDTO> request = new HttpEntity<>(adminCreateDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarAdministrador_ComAdministrador_DeveRetornar200() {
        AdministradorCreateDTO adminCreateDTO = new AdministradorCreateDTO();
        adminCreateDTO.setNome("Admin Teste");
        adminCreateDTO.setMatricula("ADM003");
        adminCreateDTO.setSenha("123456");
        adminCreateDTO.setSuperAdmin(false);
        
        restTemplate.postForEntity(baseUrl, adminCreateDTO, AdministratorResponseDTO.class);

        ResponseEntity<AdministratorResponseDTO> response = restTemplate.getForEntity(
                baseUrl, AdministratorResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void editarAdministrador_ComAdministrador_DeveRetornar200() {
        AdministradorCreateDTO adminCreateDTO = new AdministradorCreateDTO();
        adminCreateDTO.setNome("Admin Original");
        adminCreateDTO.setMatricula("ADM004");
        adminCreateDTO.setSenha("123456");
        adminCreateDTO.setSuperAdmin(false);
        
        restTemplate.postForEntity(baseUrl, adminCreateDTO, AdministratorResponseDTO.class);

        adminCreateDTO.setNome("Admin Editado");
        adminCreateDTO.setSuperAdmin(true);
        
        HttpEntity<AdministradorCreateDTO> request = new HttpEntity<>(adminCreateDTO);
        ResponseEntity<AdministratorResponseDTO> response = restTemplate.exchange(
                baseUrl, HttpMethod.PUT, request, AdministratorResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}