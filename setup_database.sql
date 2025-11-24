-- Script para configurar o banco PostgreSQL
-- Execute este script no pgAdmin ou psql

-- 1. Criar o banco de dados (se não existir)
CREATE DATABASE feedback_alunos;

-- 2. Conectar ao banco feedback_alunos e executar os comandos abaixo:

-- Inserir perfis básicos
INSERT INTO tb_perfil (nome_perfil) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO tb_perfil (nome_perfil) VALUES ('ALUNO') ON CONFLICT DO NOTHING;
INSERT INTO tb_perfil (nome_perfil) VALUES ('PROFESSOR') ON CONFLICT DO NOTHING;

-- Inserir cursos exemplo
INSERT INTO tb_curso (nome) VALUES ('Engenharia de Software') ON CONFLICT DO NOTHING;
INSERT INTO tb_curso (nome) VALUES ('Ciência da Computação') ON CONFLICT DO NOTHING;
INSERT INTO tb_curso (nome) VALUES ('Sistemas de Informação') ON CONFLICT DO NOTHING;