package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.PerfilResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.PerfilMapper;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public PerfilResponseDTO cadastrarPerfil(PerfilCreateDTO perfilCreateDTO) {
        Perfil perfil = PerfilMapper.toEntity(perfilCreateDTO);
        Perfil salvo = perfilRepository.save(perfil);
        return PerfilMapper.toDTO(salvo);
    }

    public PerfilResponseDTO editarPerfil(Long id, PerfilCreateDTO perfilCreateDTO) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + id));

        perfil.setNomePerfil(perfilCreateDTO.getNome());
        Perfil atualizado = perfilRepository.save(perfil);
        return PerfilMapper.toDTO(atualizado);
    }

    public void excluirPerfil(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new PerfilNotFoundException("Perfil não encontrado com ID: " + id);
        }
        perfilRepository.deleteById(id);
    }

    public List<PerfilResponseDTO> listarPerfis() {
        return perfilRepository.findAll().stream().map(PerfilMapper::toDTO).toList();
    }

    public PerfilResponseDTO buscarPorId(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + id));
        return PerfilMapper.toDTO(perfil);
    }

    public PerfilResponseDTO buscarPorNome(String nomePerfil) {
        Perfil perfil = perfilRepository.findByNomePerfil(nomePerfil)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com nome: " + nomePerfil));
        return PerfilMapper.toDTO(perfil);
    }
}
