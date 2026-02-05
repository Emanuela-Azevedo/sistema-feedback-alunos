-- Inserir perfis básicos (apenas se não existirem)
INSERT INTO tb_perfil (nome_perfil)
SELECT 'ROLE_ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM tb_perfil WHERE nome_perfil = 'ROLE_ADMIN'
);

INSERT INTO tb_perfil (nome_perfil)
SELECT 'ROLE_ALUNO'
    WHERE NOT EXISTS (
    SELECT 1 FROM tb_perfil WHERE nome_perfil = 'ROLE_ALUNO'
);

INSERT INTO tb_perfil (nome_perfil)
SELECT 'ROLE_PROFESSOR'
    WHERE NOT EXISTS (
    SELECT 1 FROM tb_perfil WHERE nome_perfil = 'ROLE_PROFESSOR'
);
