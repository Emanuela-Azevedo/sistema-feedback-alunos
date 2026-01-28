package com.projetoDac.feedback_alunos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;

@Service
public class RoleService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil save(Perfil perfil) {
        return perfilRepository.save(perfil);
    }
}