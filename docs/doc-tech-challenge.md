# Projeto: Sistema de Gestão de Restaurantes - Tech Challenge Fase 01

## Equipe: Lista dos nomes e RMs dos alunos

| Nome | RM |
| --- | --- |
| Emerson Pereira da Silva | RM367268 |
| Levi Aparecido do Santos | RM369031 |
| Luiz Octavio Tassinari Saraiva | RM367408|
| Rhayana Lacerda Gomes | RM367798 | 
| Vinicius Padovam Valentim | RM367199|

 
# 1. Introdução
   
## 1.1. Descrição do problema

Um grupo de restaurantes busca desenvolver um sistema de gestão unificado e compartilhado para reduzir os altos custos de soluções individuais. O objetivo é criar uma plataforma robusta que permita aos restaurantes gerenciar suas operações de forma eficiente e, ao mesmo tempo, ofereça aos clientes a possibilidade de consultar informações, fazer pedidos online e deixar avaliações.
Devido a limitações orçamentárias, o projeto será entregue em fases, permitindo uma implementação gradual, com melhorias contínuas baseadas no feedback dos restaurantes e clientes.

## 1.2. Objetivo do projeto

Desenvolver um backend completo e robusto utilizando **Java 21**, **Spring Boot 3.5.6** e **PostgreSQL 17**.
O sistema implementa **Arquitetura Hexagonal** com **Clean Architecture** e princípios **SOLID**, proporcionando:

### **Funcionalidades Core Implementadas**
- **Gestão completa de usuários**: CRUD com validações robustas e autorização por roles
- **Gestão de endereços**: CRUD completo de endereços vinculados aos usuários autenticados  
- **Autenticação JWT**: Sistema seguro stateless com tokens e autorização RBAC (Role-Based Access Control)
- **Três níveis de acesso**: Cliente (CLIENT), Proprietário (OWNER) e Administrador (ADMIN)
- **Documentação OpenAPI/Swagger**: Interface interativa para testes de API
- **Exception Handling RFC 7807**: Tratamento padronizado de erros seguindo padrões internacionais
- **Validações Jakarta Bean**: Validações declarativas em todas as camadas
- **Containerização Docker**: Docker Compose para orquestração completa

### **Arquitetura e Design Patterns**
- **Arquitetura Hexagonal (Ports & Adapters)**: Separação clara entre core e infraestrutura
- **Clean Architecture**: Dependências apontando para o centro, regras de negócio isoladas
- **Domain-Driven Design (DDD)**: Modelagem rica com Entities, Value Objects e Domain Services
- **Princípios SOLID**: Implementação rigorosa dos 5 princípios
- **CQRS Pattern**: Separação entre comandos e queries
- **Dependency Injection**: Inversão de controle através de interfaces

### **Tecnologias e Ferramentas**
- **Backend**: Java 21, Spring Boot 3.5.6, Spring Security, Spring Data JPA
- **Banco de Dados**: PostgreSQL 17 com schema e dados de teste
- **Autenticação**: JWT com algoritmo HMAC512 e BCrypt para senhas
- **Documentação**: SpringDoc OpenAPI 3.0 com Swagger UI integrado
- **Containerização**: Docker multi-stage build + Docker Compose
- **Build**: Maven 3.9 com profiles de desenvolvimento e teste
- **Testes**: JUnit 5, Spring Boot Test, H2 Database para testes

A aplicação é completamente dockerizada, utilizando Docker Compose para orquestração junto com PostgreSQL em containers isolados.

# 2. Arquitetura do Sistema

## Descrição da Arquitetura

O sistema implementa **Arquitetura Hexagonal** (Ports & Adapters) seguindo rigorosamente os princípios de **Clean Architecture** e **SOLID**. Esta abordagem proporciona:

### **Benefícios Arquiteturais**
- **Isolamento do domínio**: Lógica de negócio independente de frameworks e tecnologias externas
- **Testabilidade superior**: Interfaces bem definidas facilitam mocks e testes isolados
- **Flexibilidade tecnológica**: Mudanças de infraestrutura sem impacto no core
- **Manutenibilidade**: Código organizado com responsabilidades bem definidas
- **Escalabilidade**: Arquitetura preparada para crescimento e evolução

## 🏗️ Estrutura das Camadas

### **📦 Application Layer (Core - 192 arquivos Java)**

#### 🎯 **Domain Layer**
```
application/domain/
├── address/AddressDomain.java           # Aggregate Root para endereços
├── user/UserDomain.java                 # Aggregate Root para usuários  
├── user/RolesEnum.java                  # Enumeração de papéis
├── valueobject/
│   ├── Email.java                       # Value Object para email
│   ├── Username.java                    # Value Object para login
│   └── PersonName.java                  # Value Object para nome
├── service/
│   ├── UserDomainService.java           # Serviços de domínio de usuário
│   └── AddressDomainService.java        # Serviços de domínio de endereço
└── exception/                           # Exceções de domínio
    ├── InvalidFieldException.java
    ├── BusinessRuleException.java
    └── DomainValidationException.java
```

#### 🔌 **Ports (Contratos)**
```
application/ports/
├── inbound/                             # Use Cases (Primary Ports)
│   ├── user/                            # 7 ports para usuários
│   ├── address/                         # 4 ports para endereços
│   └── auth/AuthPort.java               # Port de autenticação
└── outbound/                            # Dependencies (Secondary Ports)
    ├── repository/
    │   ├── UserRepositoryPort.java
    │   └── AddressRepositoryPort.java
    └── security/PasswordEncoderPort.java
```

#### ⚙️ **Use Cases (Orquestradores)**
```
application/usecase/
├── user/                                
│   ├── CreateUserUseCase.java
│   ├── CreateOwnerUseCase.java
│   ├── UpdateUserUseCase.java
│   ├── UpdatePasswordUseCase.java
│   ├── FindUserByIdUseCase.java
│   ├── FindUserByNameUseCase.java
│   └── DeleteUserUseCase.java
└── address/                             
    ├── CreateAddressUseCase.java
    ├── UpdateAddressUseCase.java
    ├── FindAddressUseCase.java
    └── DeleteAddressUseCase.java
```

### **Infrastructure Layer**

#### **Inbound Adapters**
```
infrastructure/adapters/inbound/
├── web/rest/                            # REST API Layer
│   ├── controller/                      # 3 Controllers principais
│   ├── api/                             # OpenAPI interfaces
│   ├── dto/                             # DTOs da camada web
│   ├── mapper/                          # Web ↔ Application mappers
│   └── config/OpenApiConfig.java        # Configuração Swagger
└── security/                            # Camada de segurança
    ├── JwtAuthenticationFilter.java     # Filtro JWT
    ├── JwtUtil.java                     # Utilitários JWT
    └── SecurityUser.java                # User details customizado
```

#### **Outbound Adapters**
```
infrastructure/adapters/outbound/
├── repositories/                        # Implementações JPA
│   ├── user/UserRepositoryImpl.java
│   └── address/AddressRepositoryImpl.java
├── entities/                            # Entidades JPA
│   ├── JpaUserEntity.java
│   └── JpaAddressEntity.java
├── mappers/                             # Domain ↔ Entity mappers
│   ├── UserEntityMapper.java
│   └── AddressEntityMapper.java
└── security/
    └── BCryptPasswordEncoderAdapter.java
```

#### **Configuration Layer**
```
infrastructure/configs/
├── SecurityConfig.java                  # Configuração Spring Security
├── WebSecurityConfig.java              # Configurações web
├── UserUseCaseConfig.java              # Beans dos Use Cases de usuário
├── AddressUseCaseConfig.java           # Beans dos Use Cases de endereço
└── AuthUseCaseConfig.java              # Beans de autenticação
```

## Stack Tecnológico

### **Backend Core**
- **Java 21**: LTS com records, pattern matching e virtual threads
- **Spring Boot 3.5.6**: Framework principal com auto-configuração
- **Spring Security 6**: Autenticação JWT e autorização RBAC
- **Spring Data JPA**: Persistência com Hibernate 6
- **Jakarta Bean Validation**: Validações declarativas

### **Banco de Dados**
- **PostgreSQL 17**: SGBD principal com recursos modernos
- **H2 Database**: Banco em memória para testes
- **Flyway**: Migração de schema (configurado via SQL)

### **Segurança**
- **JWT (jjwt 0.12.5)**: Tokens stateless com HMAC512
- **BCrypt**: Hash de senhas com salt automático
- **RBAC**: Autorização baseada em roles (CLIENT, OWNER, ADMIN)

### **Documentação e APIs**
- **SpringDoc OpenAPI 2.8.13**: Geração automática de documentação
- **Swagger UI**: Interface interativa para testes
- **RFC 7807**: Padronização de respostas de erro

### **DevOps e Infraestrutura**
- **Docker**: Containerização multi-stage
- **Docker Compose**: Orquestração de serviços
- **Maven 3.9**: Build e gerenciamento de dependências
- **JUnit 5**: Framework de testes unitários e integração

## Diagramas e Modelagem

### Diagramas de Arquitetura
Os diagramas PlantUML da arquitetura estão disponíveis nos arquivos:
- **Visão Geral**: [`docs/diag01.puml`](diag01.puml) - Arquitetura Hexagonal completa
- **Fluxo de Autenticação**: [`docs/diag03.puml`](diag03.puml) - Sequence diagram de login
- **Interações de Sistema**: [`docs/diag02.puml`](diag02.puml) - Fluxos de Use Cases
- **Camadas da Aplicação**: [`docs/diag04.puml`](diag04.puml) - Separação de responsabilidades

### Modelo de Banco de Dados

#### **Esquema Principal** (`schema.sql`)
```sql
-- Tabela de usuários com autenticação e roles
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    roles VARCHAR(255)[] NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- Tabela de endereços vinculados aos usuários
CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES "users"(id),
    street VARCHAR(100) NOT NULL,
    number VARCHAR(20) NOT NULL,
    city VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

#### **Entidades e Relacionamentos**
- **`users`**: Aggregate Root para autenticação e perfis de usuário
- **`address`**: Entity dependente vinculada ao usuário
- **Relacionamento 1:N**: Um usuário pode ter múltiplos endereços
- **Integridade Referencial**: Foreign Key com cascade definido
- **UUIDs**: Identificadores únicos globais para segurança

#### **📊 Dados de Teste Pré-Carregados** (`data.sql`)
- **👤 7 usuários** pré-cadastrados:
  - **1 ADMIN**: Emerson Silva (acesso total)  
  - **2 OWNERS**: Vinicius Padovam, Carlos Oliveira (proprietários)
  - **4 CLIENTS**: Maria Silva, João Pereira, Ana Souza, João Silva
- **🏠 6 endereços** distribuídos em diferentes cidades (SP, RJ, PR, MG)
- **🔐 Senha padrão**: `senha123` (BCrypt hash) para todos os usuários
- **� UUIDs fixos**: Para facilitar testes e referências

# 3. Descrição dos Endpoints da API

## 📋 Tabela Completa de Endpoints

### **Autenticação**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/auth/login` | POST | Autenticar e obter token JWT | ❌ | Público | `LoginController` |

### **Gestão de Usuários**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/users` | POST | Criar usuário CLIENT | ❌ | Público | `UserController` |
| `/api/v1/users/owner` | POST | Criar usuário OWNER | ✅ | ADMIN | `UserController` |
| `/api/v1/users/{id}` | GET | Buscar usuário por ID | ✅ | ADMIN ou próprio | `UserController` |
| `/api/v1/users/by-name` | GET | Buscar por nome (query) | ✅ | ADMIN | `UserController` |
| `/api/v1/users/{id}` | PUT | Atualizar dados gerais | ✅ | ADMIN ou próprio | `UserController` |
| `/api/v1/users/{id}/password` | PUT | Alterar senha | ✅ | ADMIN ou próprio | `UserController` |
| `/api/v1/users/{id}` | DELETE | Excluir usuário | ✅ | ADMIN | `UserController` |

### **Gestão de Endereços**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/address` | GET | Listar endereços próprios | ✅ | Usuário autenticado | `AddressController` |
| `/api/v1/address` | POST | Criar endereço | ✅ | Usuário autenticado | `AddressController` |
| `/api/v1/address/{addressId}` | PUT | Atualizar endereço | ✅ | Proprietário | `AddressController` |
| `/api/v1/address/{addressId}` | DELETE | Excluir endereço | ✅ | Proprietário | `AddressController` |

### **Documentação (OpenAPI/Swagger)**
| Endpoint | Método | Descrição | Auth | Autorização |
|----------|--------|-----------|------|-------------|
| `/swagger-ui` | GET | Interface Swagger UI | ❌ | Público |
| `/v3/api-docs` | GET | Especificação OpenAPI JSON | ❌ | Público |

## 💡 Exemplos de Requisição e Resposta

### 🔐 **POST** `/api/v1/auth/login`
**Descrição**: Autenticar usuário e obter token JWT stateless

**📤 Requisição:**
```json
{
  "login": "emerson.silva",
  "password": "senha123"
}
```

**📥 Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJlbWVyc29uLnNpbHZhIiwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNzMwNDkyNDAwLCJleHAiOjE3MzA1Nzg4MDB9...",
  "type": "Bearer",
  "expiresIn": 86400
}
```

**🔧 Headers de Resposta:**
```http
Content-Type: application/json
X-Content-Type-Options: nosniff
```

### 👤 **POST** `/api/v1/users`
**Descrição**: Criar novo usuário com role CLIENT (registro público)

**📤 Requisição:**
```json
{
  "name": "João Silva Santos",
  "email": "joao.santos@example.com",
  "login": "joao.santos",
  "password": "minhasenha123"
}
```

**📥 Resposta (201 Created):**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "João Silva Santos",
  "email": "joao.santos@example.com",
  "login": "joao.santos",
  "role": ["CLIENT"],
  "createdAt": "2025-11-01T15:30:45.123Z",
  "updatedAt": "2025-11-01T15:30:45.123Z",
  "isActive": true
}
```

**🎯 Validações Aplicadas:**
- **Name**: 2-100 caracteres, não nulo
- **Email**: Formato válido e único no sistema
- **Login**: 3-50 caracteres, único no sistema  
- **Password**: Mínimo 6 caracteres (será criptografado com BCrypt)

### 🔍 **GET** `/api/v1/users/{id}`
**Descrição**: Buscar usuário específico por UUID  
**🔐 Autorização**: ADMIN ou próprio usuário

**📤 Headers:**
```http
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**📥 Resposta (200 OK):**
```json
{
  "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16",
  "name": "Emerson Silva",
  "email": "emerson.silva@example.com", 
  "login": "emerson.silva",
  "role": ["ADMIN"],
  "createdAt": "2025-11-01T10:30:00Z",
  "updatedAt": "2025-11-01T10:30:00Z",
  "isActive": true
}
```

### 🔍 **GET** `/api/v1/users/by-name?name={nome}`
**Descrição**: Buscar usuários por nome (busca parcial case-insensitive)  
**🔐 Autorização**: Apenas ADMIN

**📤 Query Parameters:**
- `name`: Fragmento do nome a buscar (mínimo 2 caracteres)

**📤 Exemplo de Requisição:**
```http
GET /api/v1/users/by-name?name=Silva
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**📥 Resposta (200 OK):**
```json
[
  {
    "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16",
    "name": "Emerson Silva",
    "email": "emerson.silva@example.com",
    "login": "emerson.silva",
    "role": ["ADMIN"]
  },
  {
    "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17",
    "name": "João Silva",
    "email": "joao.silva@example.com",
    "login": "joao.silva", 
    "role": ["CLIENT"]
  }
]
```

### ✏️ **PUT** `/api/v1/users/{id}`
**Descrição**: Atualizar dados gerais do usuário (nome, email, login)  
**🔐 Autorização**: ADMIN ou próprio usuário

**📤 Requisição:**
```json
{
  "name": "João Santos Silva Junior",
  "email": "joao.santos.junior@example.com",
  "login": "joao.junior"
}
```

**📥 Resposta (200 OK):**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "João Santos Silva Junior",
  "email": "joao.santos.junior@example.com",
  "login": "joao.junior",
  "role": ["CLIENT"],
  "createdAt": "2025-11-01T15:30:45.123Z",
  "updatedAt": "2025-11-01T16:45:32.789Z",
  "isActive": true
}
```

### 🔒 **PUT** `/api/v1/users/{id}/password`  
**Descrição**: Alterar senha do usuário (endpoint separado por segurança)  
**🔐 Autorização**: ADMIN ou próprio usuário

**📤 Requisição:**
```json
{
  "currentPassword": "minhasenha123",
  "newPassword": "novaSenhaSegura456!"
}
```

**📥 Resposta (200 OK):**
```json
{
  "message": "Password updated successfully"
}
```

**⚠️ Validações de Segurança:**
- Senha atual deve ser válida
- Nova senha deve ter mínimo 6 caracteres
- Hash BCrypt aplicado automaticamente

### ❌ **DELETE** `/api/v1/users/{id}`
**Descrição**: Exclusão definitiva de usuário do sistema  
**🔐 Autorização**: Apenas ADMIN

**📥 Resposta (204 No Content):**
```
(Sem corpo de resposta)
```

**⚠️ Comportamento:**
- Exclusão em cascata dos endereços vinculados
- Operação irreversível
- Logs de auditoria gerados automaticamente

## 🏠 **Endpoints de Gestão de Endereços**

### 📍 **GET** `/api/v1/address`
**Descrição**: Listar todos os endereços do usuário autenticado  
**🔐 Autorização**: Usuário autenticado (qualquer role)

**📥 Resposta (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "street": "Rua das Palmeiras",
    "number": "123",
    "complement": "Apto 45",
    "neighborhood": "Jardins", 
    "city": "São Paulo",
    "zipCode": "01414-000",
    "createdAt": "2025-11-01T10:30:00Z",
    "updatedAt": "2025-11-01T10:30:00Z"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "street": "Av. Paulista",
    "number": "1000",
    "complement": null,
    "neighborhood": "Bela Vista",
    "city": "São Paulo", 
    "zipCode": "01310-100",
    "createdAt": "2025-11-01T11:15:00Z",
    "updatedAt": "2025-11-01T11:15:00Z"
  }
]
```

### 🏠 **POST** `/api/v1/address`
**Descrição**: Criar novo endereço vinculado ao usuário autenticado  
**🔐 Autorização**: Usuário autenticado

**📤 Requisição:**
```json
{
  "street": "Rua Oscar Freire",
  "number": "500",
  "complement": "Loja 12", 
  "neighborhood": "Jardins",
  "city": "São Paulo",
  "zipCode": "01426-001"
}
```

**📥 Resposta (201 Created):**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d480",
  "street": "Rua Oscar Freire",
  "number": "500", 
  "complement": "Loja 12",
  "neighborhood": "Jardins",
  "city": "São Paulo",
  "zipCode": "01426-001",
  "createdAt": "2025-11-01T16:30:45.123Z",
  "updatedAt": "2025-11-01T16:30:45.123Z"
}
```

**🎯 Validações de Negócio:**
- **Street**: 3-100 caracteres obrigatórios
- **Number**: 0-20 caracteres obrigatórios (aceita vazio "")
- **Complement**: 0-50 caracteres opcionais
- **Neighborhood**: 2-50 caracteres obrigatórios  
- **City**: 2-50 caracteres obrigatórios
- **ZipCode**: 8-10 caracteres obrigatórios

### ✏️ **PUT** `/api/v1/address/{addressId}`
**Descrição**: Atualizar endereço existente  
**🔐 Autorização**: Proprietário do endereço

**📤 Requisição:**
```json
{
  "street": "Rua Augusta",
  "number": "2500",
  "complement": "Conjunto 1401",
  "neighborhood": "Consolação", 
  "city": "São Paulo",
  "zipCode": "01412-100"
}
```

**📥 Resposta (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "street": "Rua Augusta", 
  "number": "2500",
  "complement": "Conjunto 1401",
  "neighborhood": "Consolação",
  "city": "São Paulo",
  "zipCode": "01412-100",
  "createdAt": "2025-11-01T10:30:00Z",
  "updatedAt": "2025-11-01T17:22:15.456Z"
}
```

### 🗑️ **DELETE** `/api/v1/address/{addressId}`
**Descrição**: Excluir endereço do usuário  
**🔐 Autorização**: Proprietário do endereço

**📥 Resposta (204 No Content):**
```
(Sem corpo de resposta)
```

**🛡️ Segurança de Resources:**
- Usuários só podem acessar/modificar seus próprios endereços
- Verificação automática de ownership nos Use Cases
- Exception `AddressDoesNotBelongToUserException` em caso de acesso indevido

## 📊 Códigos de Status HTTP e Tratamento de Erros

### ✅ **Códigos de Sucesso**
- **200 OK**: Operação de leitura/atualização realizada com sucesso
- **201 Created**: Recurso criado com sucesso (usuário, endereço)
- **204 No Content**: Operação de exclusão realizada sem retorno de dados

### ⚠️ **Códigos de Erro do Cliente** 
- **400 Bad Request**: Dados de entrada inválidos ou malformados
- **401 Unauthorized**: Token JWT não fornecido, inválido ou expirado
- **403 Forbidden**: Usuário autenticado mas sem permissão para a operação
- **404 Not Found**: Recurso solicitado não encontrado (usuário, endereço)
- **409 Conflict**: Conflito de dados únicos (email, login já existem)

### 🔥 **Códigos de Erro do Servidor**
- **500 Internal Server Error**: Erro interno não tratado do servidor

### 🎯 **Tratamento Padronizado RFC 7807**

Todas as respostas de erro seguem o padrão **RFC 7807 (Problem Details)**:

**Exemplo - 400 Bad Request:**
```json
{
  "type": "about:blank",
  "title": "Bad Request", 
  "status": 400,
  "detail": "Validation failed for field: email",
  "instance": "/api/v1/users",
  "timestamp": "2025-11-01T17:30:45.123Z",
  "errors": [
    {
      "field": "email",
      "message": "Email deve ter formato válido"
    },
    {
      "field": "password", 
      "message": "Password deve ter pelo menos 6 caracteres"
    }
  ]
}
```

**Exemplo - 401 Unauthorized:**
```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "JWT token is invalid or expired",
  "instance": "/api/v1/users/123",
  "timestamp": "2025-11-01T17:30:45.123Z"
}
```

**Exemplo - 403 Forbidden:**
```json
{
  "type": "about:blank", 
  "title": "Forbidden",
  "status": 403,
  "detail": "User does not have permission to access this resource",
  "instance": "/api/v1/users/456", 
  "timestamp": "2025-11-01T17:30:45.123Z"
}
```

### 🛡️ **Exception Handling Global**

**Classes de Exceção Implementadas:**
- `GlobalExceptionHandler`: Captura e padroniza todas as exceções
- `UserNotFoundException`: Usuário não encontrado (404)
- `AddressNotFoundException`: Endereço não encontrado (404)  
- `UserAlreadyExistsException`: Email/login duplicado (409)
- `AddressDoesNotBelongToUserException`: Acesso negado a endereço (403)
- `InvalidFieldException`: Validação de domínio falhou (400)
- `BusinessRuleException`: Regra de negócio violada (400)

# 4. Configuração do Projeto

## 🐳 **Configuração Docker & Containerização**

### **Docker Compose Completo** (`compose.yaml`)

O projeto utiliza **Docker Compose** para orquestração completa com **networking** e **volumes** persistentes:

```yaml
services:
  # PostgreSQL 17 Database Service
  postgres:
    image: postgres:17
    environment:
      - POSTGRES_DB=restaurantapp
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=secret
    ports:
      - "5432:5432"
    networks:
      - app-network
    volumes:
      - db_data:/var/lib/postgresql/data
    labels:
      org.springframework.boot.service-connection: postgres
      
  # Spring Boot Application Service
  spring:
    build:
      context: .
      dockerfile: Dockerfile
    user: "2000:2000"        # Non-root user for security
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/restaurantapp
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=secret
    ports:
      - "8080:8080"
    networks:
      - app-network
    depends_on:
      - postgres

volumes:
  db_data:                   # Persistent database storage

networks:
  app-network:               # Isolated network for services
    driver: bridge
```

### **🏗️ Dockerfile Multi-stage** (Otimizado)

```dockerfile
# Build stage - Maven + OpenJDK 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY pom.xml ./
COPY .mvn .mvn
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime stage - JRE 21 (menor footprint)
FROM eclipse-temurin:21-jre-noble AS runtime
WORKDIR /app

# Security: Non-root user
RUN groupadd -r app && \
    useradd -r -g app -u 2000 -s /sbin/nologin -d /nonexistent app && \
    mkdir -p /app && chown -R app:app /app

COPY --from=builder --chown=2000:2000 /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java -jar /app/app.jar"]
```

**🔧 Características do Build:**
- **Multi-stage**: Reduz tamanho final da imagem
- **Security**: Usuário não-root (UID 2000)
- **Performance**: Cache de dependências Maven
- **Production-ready**: JRE-only no runtime

## 🚀 **Instruções para Execução**

### **📋 Pré-requisitos**
- **Docker** e **Docker Compose** (recomendado)
- **Java 21+** (para desenvolvimento local)
- **Maven 3.9+** (para build local)
- **Git** para versionamento

### **🐳 Opção 1: Docker Compose Completo** (Recomendada)
```bash
# Clone o repositório
git clone https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01.git
cd tech-challenge-fase-01

# Executar aplicação completa
docker compose up --build

# Ou executar em background
docker compose up -d --build
```

**✅ Vantagens:**
- Ambiente isolado e reproduzível
- PostgreSQL e aplicação configurados automaticamente
- Network isolation e persistent volumes

### **⚡ Opção 2: Desenvolvimento Híbrido**
```bash
# 1. Iniciar apenas PostgreSQL
docker compose up -d postgres

# 2. Executar Spring Boot localmente (com hot reload)
./mvnw spring-boot:run -Dspring-boot.devtools.restart.enabled=true

# 3. Para debug com breakpoints
./mvnw spring-boot:run \
  -Dspring-boot.devtools.restart.enabled=true \
  -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'
```

**✅ Vantagens:**
- Restart rápido durante desenvolvimento  
- Debug com IDE
- Logs diretos no terminal

### **🌐 URLs de Acesso**

| Serviço | URL Local | URL Docker | Descrição |
|---------|-----------|------------|-----------|
| **API REST** | http://localhost:8080 | http://localhost:8080 | Endpoints principais |
| **Swagger UI** | http://localhost:8080/swagger-ui | http://localhost:8080/swagger-ui | Documentação interativa |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | http://localhost:8080/v3/api-docs | Spec OpenAPI |
| **PostgreSQL** | localhost:5432 | postgres:5432 | Banco de dados |

### **🗄️ Configuração do Banco**

| Parâmetro | Valor | Descrição |
|-----------|-------|-----------|  
| **Host** | localhost (local) / postgres (docker) | Servidor PostgreSQL |
| **Port** | 5432 | Porta padrão PostgreSQL |
| **Database** | restaurantapp | Nome do banco |
| **Username** | postgres | Usuário administrador |
| **Password** | secret | Senha (⚠️ dev only) |
| **Version** | PostgreSQL 17 | Versão mais recente |

### **📊 Dados de Teste Disponíveis**

**👤 Usuários Pré-cadastrados:**
```bash
# ADMIN (acesso total)
Login: emerson.silva | Senha: senha123

# OWNERS (proprietários)  
Login: vpadovam     | Senha: senha123
Login: carlosol     | Senha: senha123

# CLIENTS (clientes)
Login: mariasilva   | Senha: senha123
Login: joaop        | Senha: senha123
Login: anasouza     | Senha: senha123
Login: joao.silva   | Senha: senha123
```

**🏠 Endereços:** 6 endereços pré-cadastrados vinculados aos usuários

# 5. Qualidade do Código

## 🏆 **Boas Práticas Implementadas**

### 🏗️ **Arquitetura e Design Patterns**
- **✅ Arquitetura Hexagonal (Ports & Adapters)**: Separação rigorosa entre core e infraestrutura
- **✅ Clean Architecture**: Dependências apontando para o centro, regras de negócio isoladas
- **✅ Domain-Driven Design (DDD)**: Modelagem rica com Entities, Value Objects, Domain Services
- **✅ CQRS Pattern**: Separação clara entre comandos (escrita) e queries (leitura)  
- **✅ Dependency Injection**: Inversão de controle através de interfaces bem definidas
- **✅ Factory Methods**: Criação controlada de objetos de domínio com validações

- **🔍 Code Quality**: SonarQube ready

### ⚖️ **Princípios SOLID - Implementação Completa**

#### **🎯 S - Single Responsibility Principle**
- **Use Cases**: Cada um com responsabilidade única e específica
- **Controllers**: Apenas adaptação entre web e application layers
- **Repositories**: Somente persistência, sem lógica de negócio
- **Mappers**: Conversões dedicadas entre camadas

#### **🔧 O - Open/Closed Principle**  
- **Ports**: Extensibilidade via novas implementações de interfaces
- **Adapters**: Novos adapters sem modificação do core
- **Use Cases**: Extensíveis via composition e dependency injection

#### **🔄 L - Liskov Substitution Principle**
- **Repository Implementations**: Intercambiáveis (JPA, MongoDB, etc.)
- **Password Encoders**: Substituíveis (BCrypt, SCrypt, etc.)
- **Security Adapters**: Diferentes provedores JWT

#### **🎭 I - Interface Segregation Principle**
- **Ports específicos**: Interfaces coesas por funcionalidade
- **Inbound Ports**: Contratos específicos por Use Case
- **Outbound Ports**: Abstrações mínimas e focadas

#### **⬆️ D - Dependency Inversion Principle**  
- **Infrastructure → Application**: Dependência em abstrações
- **Use Cases → Repositories**: Através de ports outbound
- **Controllers → Use Cases**: Através de ports inbound

### 🔐 **Segurança Robusta**
- **🎟️ JWT Stateless**: Tokens auto-contidos com expiração configurável
- **🛡️ RBAC (Role-Based Access Control)**: 3 níveis (CLIENT, OWNER, ADMIN)
- **🔒 BCrypt Hashing**: Senhas criptografadas com salt automático
- **✅ Jakarta Bean Validation**: Validações declarativas em todas as camadas
- **🌐 CORS Configurado**: Controle granular de origens permitidas
- **🔑 Resource Authorization**: Usuários só acessam recursos próprios

### 🚨 **Exception Handling Padronizado**
- **📋 RFC 7807 Compliance**: Problem Details para respostas de erro consistentes
- **🎯 Global Exception Handler**: Tratamento centralizado via `@ControllerAdvice`
- **📊 Error Logging**: Logs estruturados para debugging e auditoria
- **🔍 Detailed Messages**: Informações específicas sem exposição de dados sensíveis

### 🧪 **Estratégia de Testes**
- **✅ Jakarta Bean Validation**: Validações automáticas nos DTOs
- **🏗️ Estrutura de Testes**: Separação entre unitários e integração
- **🐳 Test Containers**: Testes com PostgreSQL real em containers
- **🎯 H2 Database**: Testes rápidos em memória
- **📊 Coverage Ready**: Estrutura preparada para JaCoCo

### 📝 **Organização e Documentação**
- **📦 Package by Feature**: Agrupamento por domínios funcionais (User, Address)
- **📖 OpenAPI 3.0**: Documentação interativa automática com Swagger UI
- **🏷️ Naming Conventions**: Nomenclatura consistente seguindo padrões Java
- **📚 Clean Code**: Métodos pequenos, responsabilidades bem definidas
- **💬 Javadoc**: Documentação nas interfaces principais

### ⚙️ **Configuração e DevOps**
- **🎭 Spring Profiles**: Separação clara de ambientes (dev, test, prod)
- **📄 Externalized Configuration**: Configurações via `application.yaml`
- **🐳 Docker Multi-stage**: Build otimizado com cache de dependências
- **📊 Actuator**: Endpoints de monitoramento e health checks

# 6. Collections para Teste

## 🧪 **Resources para Testes Completos**

### 📋 **Postman Collection**
**Arquivo**: `TechChallenge.postman_collection.json` 

### 📝 **HTTP Files (VS Code)**
**Arquivo**: `docs/chamadas.http` 

### 🌐 **Swagger UI Interativo**
**URL**: http://localhost:8080/swagger-ui

# 8. Repositório do Código

## 📂 **Informações do Repositório**

**🌐 URL**: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01

### **🌳 Estrutura de Branches**

## 🏆 **Conclusão do Projeto**

Este projeto demonstra a implementação **exemplar** de uma arquitetura moderna Java enterprise, integrando:

- **🏛️ Arquitetura Hexagonal** com separação rigorosa de responsabilidades
- **⚖️ Princípios SOLID** aplicados consistentemente  
- **🔐 Segurança robusta** com JWT + RBAC + resource ownership
- **📖 Documentação automática** com OpenAPI 3.0 + Swagger UI
- **🐳 Containerização** production-ready com Docker
- **🧹 Clean Code** com nomenclatura consistente e responsabilidades claras
