package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.CursoCreateDTO;
import com.projetoDac.feedback_alunos.dto.CursoResponseDTO;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.model.Curso;
import com.projetoDac.feedback_alunos.repository.CursoRepository;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private CursoCreateDTO cursoCreateDTO;
    private Curso curso;

    @BeforeEach
    void setUp() {
        cursoCreateDTO = new CursoCreateDTO();
        cursoCreateDTO.setNome("Engenharia de Software");

        curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Engenharia de Software");
    }

    @Test
    void cadastrarCurso_DeveRetornarCursoResponseDTO() {
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoResponseDTO resultado = cursoService.cadastrarCurso(cursoCreateDTO);

        assertNotNull(resultado);
        assertEquals(curso.getIdCurso(), resultado.getIdCurso());
        assertEquals(curso.getNome(), resultado.getNome());
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void editarCurso_ComIdValido_DeveRetornarCursoAtualizado() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoResponseDTO resultado = cursoService.editarCurso(1L, cursoCreateDTO);

        assertNotNull(resultado);
        assertEquals(curso.getNome(), resultado.getNome());
        verify(cursoRepository).findById(1L);
        verify(cursoRepository).save(curso);
    }

    @Test
    void editarCurso_ComIdInvalido_DeveLancarExcecao() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNotFoundException.class, () -> 
            cursoService.editarCurso(1L, cursoCreateDTO));
        
        verify(cursoRepository).findById(1L);
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void excluirCurso_ComIdValido_DeveExcluirCurso() {
        when(cursoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> cursoService.excluirCurso(1L));

        verify(cursoRepository).existsById(1L);
        verify(cursoRepository).deleteById(1L);
    }

    @Test
    void excluirCurso_ComIdInvalido_DeveLancarExcecao() {
        when(cursoRepository.existsById(1L)).thenReturn(false);

        assertThrows(CursoNotFoundException.class, () -> 
            cursoService.excluirCurso(1L));

        verify(cursoRepository).existsById(1L);
        verify(cursoRepository, never()).deleteById(any());
    }

    @Test
    void listarCursos_DeveRetornarListaDeCursos() {
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAll()).thenReturn(cursos);

        List<CursoResponseDTO> resultado = cursoService.listarCursos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(curso.getNome(), resultado.get(0).getNome());
        verify(cursoRepository).findAll();
    }
}