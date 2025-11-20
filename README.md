# Sistema de Avaliação de Disciplinas e Professores

**Equipe:** Emanuela Azevedo e Luciana Farias

##  Descrição do Projeto

O Sistema de Avaliação de Disciplinas e Professores tem como objetivo disponibilizar uma plataforma na qual os alunos possam registrar avaliações sobre as disciplinas cursadas e os professores responsáveis, contribuindo para o aprimoramento contínuo da qualidade do ensino.

A aplicação permite que os alunos realizem avaliações de forma simples, registrando notas e comentários sobre diferentes aspectos relacionados à metodologia, clareza das explicações e condução das aulas. Além disso, o sistema possibilita à equipe administrativa gerenciar usuários, cursos, disciplinas e professores, garantindo o controle das informações e dos perfis de acesso.

O propósito geral do sistema é promover um ambiente de feedback estruturado, que apoie decisões pedagógicas e fortaleça a comunicação entre alunos, docentes e coordenação.

---

## Principais Funcionalidades

- Autenticação de usuários
- Cadastro e gerenciamento de usuários
- Cadastro e gerenciamento de cursos
- Cadastro e gerenciamento de disciplinas
- Cadastro e gerenciamento de professores
- Realização de avaliações de disciplinas
- Realização de avaliações de professores
- Gerenciamento de perfis de acesso (administrador)

---

## Tecnologias Utilizadas

- **Back-end:** Spring Boot com JPA
- **Banco de Dados:** PostgreSQL
- **Front-end:** React
- **Versionamento:** Git e GitHub

---

## Requisitos Funcionais (Atualizados)

| **MÓDULO**          | **ID**  | **REQUISITO (INFINITIVO)**                |
|---------------------|---------|-------------------------------------------|
| Autenticação        | RF01    | Realização de login                       |
| Usuários            | RF02    | Cadastro de usuários                      |
| Usuários            | RF03    | Edição de usuários                        |
| Usuários            | RF04    | Exclusão de usuários                      |
| Usuários            | RF05    | Listagem de usuários                      |
| Cursos              | RF06    | Cadastro de cursos                        |
| Cursos              | RF07    | Edição de cursos                          |
| Cursos              | RF08    | Exclusão de cursos                        |
| Cursos              | RF09    | Listagem de cursos                        |
| Disciplinas         | RF10    | Cadastro de disciplinas                   |
| Disciplinas         | RF11    | Edição de disciplinas                     |
| Disciplinas         | RF12    | Exclusão de disciplinas                   |
| Disciplinas         | RF13    | Listagem de disciplinas                   |
| Professores         | RF14    | Cadastro de professores                   |
| Professores         | RF15    | Edição de professores                     |
| Professores         | RF16    | Exclusão de professores                   |
| Professores         | RF17    | Listagem de professores                   |
| Avaliações          | RF18    | Realização de avaliação de professor      |
| Avaliações          | RF19    | Realização de avaliação de disciplina     |
| Avaliações          | RF20    | Listagem de avaliações                    |
| Avaliações          | RF21    | Exclusão de avaliações                    |
| Perfis de Acesso    | RF22    | Gerenciamento de perfis                   |

---

## Modelo Lógico do Banco de Dados

O modelo lógico representa as entidades do sistema e seus relacionamentos.  
O arquivo pode ser visualizado em:

- `-doc/documentos do projeto.pdf`

---

## Casos de Uso

Os casos de uso representam as interações dos atores (Aluno, Professor e Administrador) com o sistema.

O diagrama pode ser visualizado em:

- `-doc/documentos do projeto.pdf`

---

## Como Contribuir

1. Clone o repositório:
   ```bash
   git clone https://github.com/Emanuela-Azevedo/sistema-feedback-alunos.git
