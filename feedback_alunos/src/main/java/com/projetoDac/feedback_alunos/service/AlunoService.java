package com.projetoDac.feedback_alunos.service;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	// Cadastrar aluno simples
	public AlunoResponseDTO cadastrarAluno(AlunoCreateDTO dto) {
		Aluno aluno = AlunoMapper.toEntity(dto);
		Aluno salvo = alunoRepository.save(aluno);
		return AlunoMapper.toDTO(salvo);
	}

	// Cadastrar aluno COMPLETO (Usuario + Perfil + Aluno)
	@Transactional
	public AlunoResponseDTO cadastrarAlunoCompleto(AlunoCreateDTO dto) {

		// 1. Criar Usuário
		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setMatricula(dto.getMatricula());
		usuario.setSenha(dto.getSenha());

		// 2. Atribuir perfil ALUNO
		Optional<Perfil> perfilAluno = perfilRepository.findByNomePerfil("ALUNO");
		perfilAluno.ifPresent(p -> usuario.getPerfis().add(p));

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// 3. Criar Aluno
		Aluno aluno = new Aluno();
		aluno.setUsuario(usuarioSalvo);
		aluno.setNome(dto.getNome());
		aluno.setMatricula(dto.getMatricula());
		aluno.setCurso(dto.getCurso());

		Aluno alunoSalvo = alunoRepository.save(aluno);

		return AlunoMapper.toDTO(alunoSalvo);
	}

	// Editar aluno
	public AlunoResponseDTO editarAluno(Long id, AlunoCreateDTO dto) {
		Aluno aluno = alunoRepository.findById(id)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com ID: " + id));

		aluno.setNome(dto.getNome());
		aluno.setCurso(dto.getCurso());
		aluno.setMatricula(dto.getMatricula());

		Aluno atualizado = alunoRepository.save(aluno);
		return AlunoMapper.toDTO(atualizado);
	}

	// Excluir aluno
	public void excluirAluno(Long id) {
		if (!alunoRepository.existsById(id)) {
			throw new AlunoNotFoundException("Aluno não encontrado com ID: " + id);
		}
		alunoRepository.deleteById(id);
	}

	// Listar todos
	public List<AlunoResponseDTO> listarAlunos() {
		return alunoRepository.findAll().stream().map(AlunoMapper::toDTO).toList();
	}

	// Buscar por ID
	public AlunoResponseDTO buscarPorId(Long id) {
		Aluno aluno = alunoRepository.findById(id)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com ID: " + id));
		return AlunoMapper.toDTO(aluno);
	}

	// Buscar por matrícula
	public AlunoResponseDTO buscarPorMatricula(String matricula) {
		Aluno aluno = alunoRepository.findByMatricula(matricula)
				.orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com matrícula: " + matricula));
		return AlunoMapper.toDTO(aluno);
	}
}
