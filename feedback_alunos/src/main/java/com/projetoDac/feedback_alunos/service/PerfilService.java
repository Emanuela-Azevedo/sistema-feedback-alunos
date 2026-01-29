package com.projetoDac.feedback_alunos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.dto.PerfilCreateDTO;
import com.projetoDac.feedback_alunos.dto.mapper.PerfilMapper;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil cadastrarPerfil(PerfilCreateDTO perfilCreateDTO) {
        Perfil perfil = PerfilMapper.toEntity(perfilCreateDTO);
        return perfilRepository.save(perfil);
    }

    public Perfil editarPerfil(Long id, PerfilCreateDTO perfilCreateDTO) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + id));

        perfil.setNomePerfil(perfilCreateDTO.getNome());
        return perfilRepository.save(perfil);
    }

    public void excluirPerfil(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new PerfilNotFoundException("Perfil não encontrado com ID: " + id);
        }
        perfilRepository.deleteById(id);
    }

    public List<Perfil> listarPerfis() {
        return perfilRepository.findAll();
    }

    public Perfil buscarPorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + id));
    }

    public Perfil buscarPorNome(String nomePerfil) {
        return perfilRepository.findByNomePerfil(nomePerfil)
                .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com nome: " + nomePerfil));
    }

    public Perfil save(Perfil perfil) {
        return perfilRepository.save(perfil);
    }
}