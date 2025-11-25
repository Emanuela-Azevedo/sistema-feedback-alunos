package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.UsuarioCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioResponseDTO;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioCreateDTO usuarioCreateDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNome("João Silva");
        usuario.setMatricula("USR001");
        usuario.setSenha("senha123");

        usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNome("João Silva");
        usuarioCreateDTO.setMatricula("USR001");
        usuarioCreateDTO.setSenha("senha123");
    }

    @Test
    void cadastrarUsuario_DeveRetornarUsuarioResponseDTO() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.cadastrarUsuario(usuarioCreateDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("USR001", resultado.getMatricula());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void listarUsuarios_DeveRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
    }

    @Test
    void buscarPorId_ComIdValido_DeveRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void buscarPorId_ComIdInvalido_DeveLancarExcecao() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.buscarPorId(1L);
        });
    }

    @Test
    void excluirUsuario_ComIdValido_DeveExcluir() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.excluirUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void excluirUsuario_ComIdInvalido_DeveLancarExcecao() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.excluirUsuario(1L);
        });
    }
}