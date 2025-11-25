package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Perfil perfil;
    private PerfilCreateDTO perfilCreateDTO;

    @BeforeEach
    void setUp() {
        perfil = new Perfil();
        perfil.setIdPerfil(1L);
        perfil.setNomePerfil("ALUNO");

        perfilCreateDTO = new PerfilCreateDTO();
        perfilCreateDTO.setNome("ALUNO");
    }

    @Test
    void cadastrarPerfil_DeveRetornarPerfilResponseDTO() {
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        PerfilResponseDTO resultado = perfilService.cadastrarPerfil(perfilCreateDTO);

        assertNotNull(resultado);
        assertEquals("ALUNO", resultado.getNome());
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    void listarPerfis_DeveRetornarListaDePerfis() {
        when(perfilRepository.findAll()).thenReturn(Arrays.asList(perfil));

        List<PerfilResponseDTO> resultado = perfilService.listarPerfis();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ALUNO", resultado.get(0).getNome());
    }

    @Test
    void buscarPorId_ComIdValido_DeveRetornarPerfil() {
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));

        PerfilResponseDTO resultado = perfilService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("ALUNO", resultado.getNome());
    }

    @Test
    void buscarPorId_ComIdInvalido_DeveLancarExcecao() {
        when(perfilRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PerfilNotFoundException.class, () -> {
            perfilService.buscarPorId(1L);
        });
    }

    @Test
    void excluirPerfil_ComIdValido_DeveExcluir() {
        when(perfilRepository.existsById(1L)).thenReturn(true);

        perfilService.excluirPerfil(1L);

        verify(perfilRepository).deleteById(1L);
    }

    @Test
    void excluirPerfil_ComIdInvalido_DeveLancarExcecao() {
        when(perfilRepository.existsById(1L)).thenReturn(false);

        assertThrows(PerfilNotFoundException.class, () -> {
            perfilService.excluirPerfil(1L);
        });
    }
}