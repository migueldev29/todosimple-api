# 📝 TodoSimple API

API RESTful para gerenciamento de tarefas (To-Do), desenvolvida com **Spring Boot**, autenticação **JWT** e suporte a deploy via **Docker**.

---

## 🚀 Funcionalidades

- ✅ Criar tarefas com descrição
- 📋 Listar todas as tarefas
- ✏️ Atualizar tarefas existentes
- 🗑️ Deletar tarefas
- 🔐 Autenticação e autorização com **Spring Security + JWT**

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 2.7.2 |
| Spring Security | 2.7.3 |
| Spring Data JPA | 2.7.3 |
| MySQL | 5.7 |
| H2 Database | 2.1.214 (testes) |
| Lombok | 1.18.24 |
| JJWT | 0.11.5 |
| Docker / Docker Compose | — |
| Maven | — |

---

## 📁 Estrutura do Projeto

```
todosimple-api/
├── src/
│   └── main/
│       ├── java/com/migueldev/todosimple/
│       └── resources/  # Frontend (HTML, JS, CSS)              
├── .env                # Variáveis de ambiente
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## ⚙️ Pré-requisitos

- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)

---

## 🐳 Rodando com Docker Compose

1. **Clone o repositório:**

```bash
git clone https://github.com/migueldev29/todosimple-api.git
cd todosimple-api
```

2. **Configure o arquivo `.env`** na raiz do projeto com as variáveis necessárias:

```env
MYSQLDB_ROOT_PASSWORD=sua_senha
MYSQLDB_DATABASE=todosimple
MYSQLDB_USER=root
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
```

3. **Suba os containers:**

```bash
docker-compose up --build
```

A aplicação estará disponível em: `http://localhost:8080`

---

## 💻 Rodando Localmente (sem Docker)

1. Configure um banco de dados MySQL localmente.
2. Ajuste as propriedades de conexão em `src/main/resources/application.properties`.
3. Execute:

```bash
./mvnw spring-boot:run
```

> Para rodar os testes com H2 (banco em memória), nenhuma configuração extra é necessária.

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)**. Para acessar os endpoints protegidos:

1. Faça login e obtenha o token JWT.
2. Inclua o token no header de todas as requisições:

```
Authorization: Bearer <seu_token>
```

---

## 📌 Endpoints Principais

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/user` | Criar usuário | ❌ |
| `POST` | `/login` | Autenticar e obter JWT | ❌ |
| `GET` | `/task` | Listar tarefas do usuário | ✅ |
| `POST` | `/task` | Criar nova tarefa | ✅ |
| `PUT` | `/task/{id}` | Atualizar tarefa | ✅ |
| `DELETE` | `/task/{id}` | Deletar tarefa | ✅ |

---

## 🌐 Frontend

O diretório `src/main/resources/static` contém uma interface simples em HTML, CSS e JavaScript para interagir com a API diretamente pelo navegador.

---

## 👤 Autor

Desenvolvido por **[migueldev29](https://github.com/migueldev29)**

---

## 📄 Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.
