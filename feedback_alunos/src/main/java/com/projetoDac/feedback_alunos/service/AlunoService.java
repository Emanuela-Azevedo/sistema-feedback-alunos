package com.projetoDac.feedback_alunos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoDac.feedback_alunos.dto.AlunoCreateDTO;
import com.projetoDac.feedback_alunos.dto.AlunoResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.AlunoMapper;
import com.projetoDac.feedback_alunos.exception.AlunoNotFoundException;
import com.projetoDac.feedback_alunos.model.Aluno;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.AlunoRepository;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Transactional
	public AlunoResponseDTO cadastrarAlunoCompleto(AlunoCreateDTO dto) {

		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setMatricula(dto.getMatricula());
		usuario.setSenha(dto.getSenha());

		Optional<Perfil> perfilAluno = perfilRepository.findByNomePerfil("ALUNO");
		perfilAluno.ifPresent(p -> usuario.getPerfis().add(p));

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		Aluno aluno = new Aluno();
		aluno.setUsuario(usuarioSalvo);
		aluno.setNome(dto.getNome());
		aluno.setMatricula(dto.getMatricula());
		aluno.setCurso(dto.getCurso());

		Aluno alunoSalvo = alunoRepository.save(aluno);

		return AlunoMapper.toDTO(alunoSalvo);
	}

	public AlunoResponseDTO editarAluno(String matricula, AlunoCreateDTO dto) {
		Aluno aluno = alunoRepository.findByMatricula(matricula)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com matrícula: " + matricula));

		aluno.setNome(dto.getNome());
		aluno.setCurso(dto.getCurso());
		aluno.setMatricula(dto.getMatricula());

		Aluno atualizado = alunoRepository.save(aluno);
		return AlunoMapper.toDTO(atualizado);
	}

	public void excluirAluno(String matricula) {
		Aluno aluno = alunoRepository.findByMatricula(matricula)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com matrícula: " + matricula));
		alunoRepository.delete(aluno);
	}

	public List<AlunoResponseDTO> listarAlunos() {
		return alunoRepository.findAll().stream().map(AlunoMapper::toDTO).toList();
	}

	public AlunoResponseDTO buscarPorMatricula(String matricula) {
		Aluno aluno = alunoRepository.findByMatricula(matricula)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com matrícula: " + matricula));
		return AlunoMapper.toDTO(aluno);
	}
}
