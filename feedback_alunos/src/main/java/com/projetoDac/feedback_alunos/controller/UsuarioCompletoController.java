package com.projetoDac.feedback_alunos.controller;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoUpdateDTO;
import com.projetoDac.feedback_alunos.dto.mapper.UsuarioCompletoMapper;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/usuarios")
public class UsuarioCompletoController {

    private final UsuarioCompletoService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioCompletoResponseDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioCompletoResponseDTO> response = usuarios.stream()
                .map(UsuarioCompletoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(UsuarioCompletoMapper.toDTO(usuario));
        } catch (Exception e) {
            log.error("Usuário não encontrado com id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @GetMapping("/matricula/{matricula}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarPorMatricula(@PathVariable String matricula) {
        try {
            Usuario usuario = usuarioService.buscarPorMatricula(matricula);
            return ResponseEntity.ok(UsuarioCompletoMapper.toDTO(usuario));
        } catch (Exception e) {
            log.error("Usuário não encontrado com matrícula {}: {}", matricula, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id,
                                              @RequestBody UsuarioCompletoUpdateDTO dto) {
        try {
            Usuario usuarioExistente = usuarioService.buscarPorId(id);

            Usuario usuarioEntity = UsuarioCompletoMapper.toEntityUpdate(dto, usuarioExistente);

            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioEntity, dto.getPerfil());

            return ResponseEntity.ok(UsuarioCompletoMapper.toDTO(usuarioAtualizado));
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        try {
            usuarioService.excluirUsuario(id);
            log.info("Usuário excluído: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir usuário {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @PostMapping("/aluno")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarAluno(@Valid @RequestBody UsuarioCompletoCreateDTO dto) {
        try {
            Usuario usuarioEntity = UsuarioCompletoMapper.toEntity(dto);
            Usuario usuarioSalvo = usuarioService.save(usuarioEntity, dto.getPerfil());
            UsuarioCompletoResponseDTO response = UsuarioCompletoMapper.toDTO(usuarioSalvo);
            log.info("Aluno criado: {}", response.getMatricula());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erro ao criar aluno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/professor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarProfessor(@Valid @RequestBody UsuarioCompletoCreateDTO dto) {
        try {
            Usuario usuarioEntity = UsuarioCompletoMapper.toEntity(dto);
            Usuario usuarioSalvo = usuarioService.save(usuarioEntity, dto.getPerfil());
            UsuarioCompletoResponseDTO response = UsuarioCompletoMapper.toDTO(usuarioSalvo);
            log.info("Professor criado: {}", response.getMatricula());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erro ao criar professor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> criarAdmin(@Valid @RequestBody UsuarioCompletoCreateDTO dto) {
        try {
            Usuario usuarioEntity = UsuarioCompletoMapper.toEntity(dto);
            Usuario usuarioSalvo = usuarioService.save(usuarioEntity,dto.getPerfil());
            UsuarioCompletoResponseDTO response = UsuarioCompletoMapper.toDTO(usuarioSalvo);
            log.info("Administrador criado: {}", response.getMatricula());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erro ao criar administrador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/professores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioCompletoResponseDTO>> listarProfessores() {
        List<Usuario> professores = usuarioService.listarUsuariosPorPerfil("ROLE_PROFESSOR");
        List<UsuarioCompletoResponseDTO> response = professores.stream()
                .map(UsuarioCompletoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professores/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO')")
    public ResponseEntity<List<UsuarioCompletoResponseDTO>> listarProfessoresPorCurso(@PathVariable Long idCurso) {
        List<Usuario> professores = usuarioService.listarProfessoresPorCurso(idCurso);
        List<UsuarioCompletoResponseDTO> response = professores.stream()
                .map(UsuarioCompletoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }
}