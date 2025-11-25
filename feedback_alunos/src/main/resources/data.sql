-- Inserir perfis básicos (apenas se não existirem)
INSERT INTO tb_perfil (nome_perfil) 
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM tb_perfil WHERE nome_perfil = 'ADMIN');

INSERT INTO tb_perfil (nome_perfil) 
SELECT 'ALUNO' WHERE NOT EXISTS (SELECT 1 FROM tb_perfil WHERE nome_perfil = 'ALUNO');

INSERT INTO tb_perfil (nome_perfil) 
SELECT 'PROFESSOR' WHERE NOT EXISTS (SELECT 1 FROM tb_perfil WHERE nome_perfil = 'PROFESSOR');

-- Inserir cursos exemplo (apenas se não existirem)
INSERT INTO tb_curso (nome) 
SELECT 'Engenharia de Software' WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE nome = 'Engenharia de Software');

INSERT INTO tb_curso (nome) 
SELECT 'Ciência da Computação' WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE nome = 'Ciência da Computação');