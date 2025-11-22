package com.projetoDac.feedback_alunos.service;

public class ProfessorService {

<<<<<<< Updated upstream
=======
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    public ProfessorResponseDTO cadastrarProfessor(ProfessorCreateDTO professorCreateDTO) {
        Professor professor = ProfessorMapper.toEntity(professorCreateDTO);
        Professor salvo = professorRepository.save(professor);
        return ProfessorMapper.toDTO(salvo);
    }

    @Transactional
    public ProfessorResponseDTO cadastrarProfessorCompleto(ProfessorCreateDTO dto) {
        // 1. Criar Usuario
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(dto.getSenha());
        
        // 2. Atribuir perfil PROFESSOR
        Optional<Perfil> perfilProfessor = perfilRepository.findByNomePerfil("PROFESSOR");
        if (perfilProfessor.isPresent()) {
            usuario.getPerfis().add(perfilProfessor.get());
        }
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        // 3. Criar Professor
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

    public ProfessorResponseDTO buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com ID: " + id));
        return ProfessorMapper.toDTO(professor);
    }

    public ProfessorResponseDTO buscarPorMatricula(String matricula) {
        Professor professor = professorRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor não encontrado com matrícula: " + matricula));
        return ProfessorMapper.toDTO(professor);
    }
>>>>>>> Stashed changes
}
