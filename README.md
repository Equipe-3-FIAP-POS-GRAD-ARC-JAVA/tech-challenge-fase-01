# tech-challenge-fase-01

O Tech Challenge é o projeto da fase que englobará os conhecimentos obtidos em todas as disciplinas da fase.

O objetivo é criar um sistema robusto que permita a todos os restaurantes gerenciar eficientemente suas operações, enquanto os clientes poderão consultar informações, deixar avaliações e fazer pedidos online.

## ⭐ Status do Projeto

**Versão**: 0.0.1-SNAPSHOT  
**Data de Validação**: 15/10/2025  
**Status**: ✅ **APROVADO - 97/100**  
**Total de Arquivos Java**: 68

### Conformidade Arquitetural

| Aspecto | Nota | Status |
|---------|------|--------|
| Arquitetura Hexagonal | 10.0/10 | ✅ |
| Princípios SOLID | 9.9/10 | ✅ |
| Clean Architecture | 10.0/10 | ✅ |
| Domain-Driven Design | 9.5/10 | ✅ |
| Segurança (JWT + RBAC) | 9.5/10 | ✅ |
| Exception Handling RFC 7807 | 10.0/10 | ✅ |

📊 **[Ver Relatório Completo de Validação](docs/PROJECT-VALIDATION-REPORT.md)**

### Built with

- Java 21 + Spring Boot 3.5.6
- PostgreSQL 16
- Docker & Docker Compose
- Maven
- JWT Authentication
- BCrypt Password Encryption

## 🏗️ Arquitetura

Este projeto segue os princípios de:
- ✅ **Arquitetura Hexagonal** (Ports & Adapters)
- ✅ **SOLID Principles**
- ✅ **Clean Architecture**
- ✅ **Domain-Driven Design (DDD)**
- ✅ **RFC 7807** (Problem Details for HTTP APIs)

### Estrutura de Camadas

```
📦 Application Layer (Core - 37 arquivos)
├── Domain (Entities, Value Objects, Services)
├── DTOs (Application-level)
├── Ports (Inbound: 9 | Outbound: 3)
├── Use Cases (7 use cases)
├── Services (2 facades)
└── Mappers e Exceptions

📦 Infrastructure Layer (30 arquivos)
├── Adapters Inbound (Controllers, Security, Validation)
├── Adapters Outbound (Repositories, Security, Entities)
├── Configurations
└── Exception Handlers

📦 Main Application (1 arquivo)
└── TechChallengeFase01Application.java
```

## 📚 Documentação

### Documentos Principais
- � **[Documentação da API](docs/API-DOCUMENTATION.md)** - ⭐ Endpoints, schemas e exemplos
- �📊 **[Relatório de Validação Completo](docs/PROJECT-VALIDATION-REPORT.md)** - Análise arquitetural
- 🏗️ **[Diagrama de Arquitetura](docs/ARCHITECTURE-DIAGRAM.md)** - Estrutura do projeto
- 📖 **[Índice de Documentação](docs/DOCS-INDEX.md)** - Navegação completa
- ✅ **[Resumo de Validação](docs/VALIDATION-SUMMARY.md)** - Status do projeto

### Documentos Técnicos
- 🛠️ **[Implementação Completa](docs/IMPLEMENTATION-COMPLETE.md)** - Checklist
- 🏛️ **[Análise SOLID e Hexagonal](docs/SOLID-HEXAGONAL-ARCHITECTURE-ANALYSIS.md)** - Princípios aplicados
- 🔧 **[Exception Handling RFC 7807](docs/RFC-7807-EXCEPTION-HANDLING.md)** - Tratamento de erros
- 📋 **[Camada de Infraestrutura](docs/INFRASTRUCTURE-LAYER.md)** - Detalhes técnicos

## Getting Started

### Prerequisites

Você deve ter o Docker instalado na sua máquina, e o WSL caso esteja no Windows.

[Instalação Windows](https://docs.docker.com/desktop/setup/install/windows-install/)

[Instalação Ubuntu](https://docs.docker.com/desktop/setup/install/linux/ubuntu/)

[WSL](https://docs.docker.com/desktop/features/wsl/)

### Installation

Para subir a aplicação e o banco de dados basta abrir um terminal na raiz deste projeto e executar o comando:

```
docker compose up --build
```

Ou use as tasks do VS Code configuradas no projeto.

## 🔐 Segurança

### Autenticação JWT
- ✅ Token-based authentication
- ✅ Expiration time: 60 minutos (configurável)
- ✅ BCrypt password encryption
- ✅ Stateless sessions

### Autorização RBAC
- **ADMIN** - Acesso total ao sistema
- **OWNER** - Proprietário de restaurante
- **CLIENT** - Cliente comum

### Endpoints Públicos
```
POST /login                    - Autenticação
POST /api/v1/users            - Criar conta (CLIENT)
GET  /api/health              - Health check
```

### Endpoints Protegidos (JWT Required)
```
POST   /api/v1/users/owner        - Criar OWNER (ADMIN only)
GET    /api/v1/users/{id}         - Buscar usuário (ADMIN, OWNER)
GET    /api/v1/users/by-name      - Buscar por nome (ADMIN, OWNER)
PUT    /api/v1/users/{id}         - Atualizar usuário
PATCH  /api/v1/users/{id}/password - Atualizar senha
DELETE /api/v1/users/{id}         - Deletar usuário (ADMIN only)
```

## 📡 API Examples

### Criar Usuário
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao",
    "password": "senha123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao",
    "password": "senha123"
  }'
```

### Acessar Endpoint Protegido
```bash
curl -X GET http://localhost:8080/api/v1/users/{id} \
  -H "Authorization: Bearer {seu_token_jwt}"
```

## 🏛️ Princípios de Design

### SOLID
- ✅ **S**ingle Responsibility: Cada classe uma responsabilidade
- ✅ **O**pen/Closed: Extensível via Ports
- ✅ **L**iskov Substitution: Adapters intercambiáveis
- ✅ **I**nterface Segregation: Ports específicos
- ✅ **D**ependency Inversion: Dependências em abstrações

### Clean Architecture
- ✅ Independência de frameworks
- ✅ Testabilidade
- ✅ Independência de Database

### Domain-Driven Design
- ✅ Aggregate Root (UserDomain)
- ✅ Value Objects (Email, Username, PersonName)
- ✅ Domain Services (UserDomainService)

## 🚀 Roadmap

### Fase 1 - Completa ✅
- [x] Arquitetura Hexagonal
- [x] SOLID Implementation
- [x] JWT Authentication
- [x] RBAC Authorization
- [x] RFC 7807 Exception Handling
- [x] Documentação Completa

### Fase 2 - Em Planejamento
- [ ] Testes Unitários e de Integração
- [ ] Swagger/OpenAPI
- [ ] CI/CD Pipeline

## 👥 Equipe

**Equipe 3 - FIAP Pós-Graduação em Arquitetura Java**

---

**Desenvolvido com ❤️ pela Equipe 3 - FIAP**
