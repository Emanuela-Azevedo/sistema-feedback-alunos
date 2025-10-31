# Sistema de Avaliação de Disciplinas e Professores

**Equipe:** Emanuela Azevedo e Luciana Farias

## Descrição do Projeto

O Sistema de Avaliação de Disciplinas e Professores tem como objetivo oferecer uma plataforma onde os alunos possam avaliar as disciplinas cursadas e os professores responsáveis, contribuindo para o aprimoramento contínuo da qualidade do ensino.

A aplicação permite que os alunos realizem avaliações de forma simples e segura, fornecendo feedback sobre diversos aspectos, como metodologia de ensino, clareza nas explicações, cumprimento do conteúdo programado e recursos utilizados em sala. Além disso, o sistema disponibiliza relatórios de desempenho e análises de satisfação que poderão ser utilizados pela coordenação e pelos professores para identificar pontos fortes e oportunidades de melhoria.

## Principais Funcionalidades

- Cadastro e login de alunos, professores e administrador
- Avaliação de disciplinas e professores com critérios de nota (1 a 5) e comentários
- Avaliações anônimas ou identificadas
- Relatórios com médias e estatísticas de satisfação e feedback
- Área administrativa para acompanhar resultados e gerar relatórios
- Cadastro e gerenciamento de disciplinas e professores

## Tecnologias Utilizadas

- **Back-end:** Spring Boot com JPA
- **Banco de Dados:** PostgreSQL
- **Front-end:** React
- **Controle de Versão:** Git e GitHub

## Requisitos Funcionais

| **MÓDULO**       | **ID**  | **NOME DO REQUISITO**                     | **MUTABILIDADE** | **PRIORIDADE** |
|------------------|---------|-------------------------------------------|------------------|----------------|
| Usuário          | RF01    | Cadastro de Usuários                      | Média            | Alta           |
| Usuário          | RF02    | Edição de Usuários                        | Alta             | Média          |
| Usuário          | RF03    | Exclusão de Usuários                      | Média            | Média          |
| Disciplina       | RF04    | Cadastro de Disciplinas                   | Baixa            | Alta           |
| Disciplina       | RF05    | Edição de Disciplinas                     | Média            | Média          |
| Disciplina       | RF06    | Exclusão de Disciplinas                   | Média            | Média          |
| Avaliação        | RF07    | Avaliação de Disciplinas                  | Alta             | Alta           |
| Avaliação        | RF08    | Avaliação de Professores                  | Alta             | Alta           |
| Avaliação        | RF09    | Edição de Avaliações                      | Alta             | Média          |
| Avaliação        | RF10    | Exclusão de Avaliações                    | Média            | Média          |
| Relatório        | RF11    | Geração de Relatórios de Satisfação       | Baixa            | Alta           |
| Relatório        | RF12    | Geração de Relatórios de Feedback         | Baixa            | Alta           |
| Administração    | RF13    | Controle de Acesso Administrativo         | Baixa            | Alta           |

## Modelo Lógico do Banco de Dados

O modelo lógico representa os relacionamentos entre usuários, alunos, professores, disciplinas, avaliações e relatórios. Você pode visualizar o diagrama completo nos arquivos:

- `doc/modelo-logico.pdf` → imagem do diagrama

## Casos de Uso

Os casos de uso representam as interações entre os diferentes perfis do sistema — aluno, professor e administrador — com as funcionalidades disponíveis. Eles ajudam a visualizar os fluxos principais da aplicação e os objetivos de cada usuário.
Você pode visualizar o diagrama completo nos arquivos:

- `doc/casos-de-uso.astah` → imagem do diagrama no formato astah

## Como Contribuir

1. Clone o repositório:
   ```bash
   git clone https://github.com/Emanuela-Azevedo/sistema-feedback-alunos.git

    Veja este vídeo para mais detalhes: 

https://youtu.be/XhIvYin3rxw?si=vprS7eI4_znwahUm
