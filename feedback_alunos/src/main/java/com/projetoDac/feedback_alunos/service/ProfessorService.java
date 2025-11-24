package com.projetoDac.feedback_alunos.service;

import com.projetoDac.feedback_alunos.dto.ProfessorCreateDTO;
import com.projetoDac.feedback_alunos.dto.ProfessorResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.ProfessorMapper;
import com.projetoDac.feedback_alunos.exception.ProfessorNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Professor;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.ProfessorRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;


    @Transactional
    public ProfessorResponseDTO cadastrarProfessorCompleto(ProfessorCreateDTO dto) {
 
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        
        Optional<Perfil> perfilProfessor = perfilRepository.findByNomePerfil("PROFESSOR");
        if (perfilProfessor.isPresent()) {
            usuario.getPerfis().add(perfilProfessor.get());
        }
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        Professor professor = new Professor();
        professor.setUsuario(usuarioSalvo);
        professor.setNome(dto.getNome());
        professor.setMatricula(dto.getMatricula());
        professor.setEspecialidade(dto.getEspecialidade());
        
        Professor professorSalvo = professorRepository.save(professor);
        return ProfessorMapper.toDTO(professorSalvo);
    }

    public ProfessorResponseDTO editarProfessor(Long id, ProfessorCreateDTO professorCreateDTO) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + id));

        professor.setNome(professorCreateDTO.getNome());
        professor.setEspecialidade(professorCreateDTO.getEspecialidade());
        professor.setMatricula(professorCreateDTO.getMatricula());

        Professor atualizado = professorRepository.save(professor);
        return ProfessorMapper.toDTO(atualizado);
    }

    public void excluirProfessor(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ProfessorNotFoundException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    public List<ProfessorResponseDTO> listarProfessores() {
        return professorRepository.findAll()
                .stream()
                .map(ProfessorMapper::toDTO)
                .toList();
    }
    public ProfessorResponseDTO buscarPorMatricula(String matricula) {
        Professor professor = professorRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com matrícula: " + matricula));
        return ProfessorMapper.toDTO(professor);
    }
}
