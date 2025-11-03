# 📡 API Documentation - Tech Challenge Fase 01

**Versão da API**: v1  
**Base URL**: `http://localhost:8080`  
**Última Atualização**: 01/11/2025
**Formato**: REST/JSON

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Autenticação](#autenticação)
- [Endpoints](#endpoints)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Address](#address)
- [Schemas](#schemas)
- [Error Handling](#error-handling)
- [Postman Collection](#postman-collection)

---

## 🎯 Visão Geral

### Características da API

- ✅ **REST** - Arquitetura RESTful
- ✅ **JSON** - Content-Type: application/json
- ✅ **JWT** - Autenticação via Bearer Token
- ✅ **RBAC** - Autorização baseada em roles (CLIENT, OWNER, ADMIN)
- ✅ **RFC 7807** - Error responses padronizados
- ✅ **Stateless** - Sem gerenciamento de sessão
- ✅ **CORS** - Cross-Origin Resource Sharing habilitado
- ✅ **Address Management** - Gerenciamento de endereços de usuários

### Versões Suportadas

| Versão | Status | Base Path |
|--------|--------|-----------|
| v1 | ✅ Atual | `/api/v1` |

### Tecnologias

- **Framework**: Spring Boot 3.5.6
- **Java**: 21
- **Database**: PostgreSQL 16
- **Security**: Spring Security + JWT
- **Validation**: Jakarta Bean Validation
- **Architecture**: Hexagonal Architecture

---

## 🔐 Autenticação

### JWT Bearer Token

A API utiliza **JSON Web Tokens (JWT)** para autenticação.

#### Como Obter o Token

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "login": "usuario",
  "password": "senha123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "userProfile": {
    "id": 123,
    "email": "usuario@exemplo.com",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  }
}
```

#### Como Usar o Token

Inclua o token no header `Authorization` de todas as requisições protegidas:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Expiração

- **Default**: 60 minutos
- **Configurável** via `JWT_EXPIRATION_MINUTE`
- **Refresh**: Não implementado (fazer novo login)

### Roles (RBAC)

| Role | Descrição | Permissões |
|------|-----------|------------|
| **ADMIN** | Administrador do sistema | Acesso total, criar OWNER, deletar usuários |
| **OWNER** | Proprietário de restaurante | Gerenciar restaurantes, visualizar usuários |
| **CLIENT** | Cliente comum | Gerenciar próprio perfil, fazer pedidos |

---

## 📡 Endpoints

### Authentication

#### POST /api/v1/auth/login

Autentica um usuário e retorna um token JWT.

**Endpoint**: `POST /api/v1/auth/login`  
**Autenticação**: ❌ Público  
**Content-Type**: `application/json`

**Request Body:**
```json
{
  "login": "string",      // required, min: 3 chars
  "password": "string"    // required, min: 6 chars
}
```

**Response 200 - Success:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "refreshToken": "optional.refresh.token.if.used...",
  "userProfile": {
    "id": 123,
    "email": "usuario@exemplo.com",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  }
}
```

**Response 401 - Unauthorized:**
```json
{
  "type": "https://api.fiap.com.br/problems/unauthorized",
  "title": "Não Autorizado",
  "status": 401,
  "detail": "Usuário ou senha inválidos",
  "instance": "/login",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao",
    "password": "senha123"
  }'
```

---

### Users

#### POST /api/v1/users

Cria um novo usuário com role CLIENT (endpoint público). Os campos de endereço são obrigatórios na criação do usuário.

**Endpoint**: `POST /api/v1/users`  
**Autenticação**: ❌ Público  
**Content-Type**: `application/json`

**Request Body:**
```json
{
  "name": "string",           // required, min: 3 chars, max: 100 chars
  "email": "string",          // required, valid email format
  "login": "string",          // required, min: 3 chars, max: 50 chars, unique
  "password": "string",       // required, min: 6 chars
  "street": "string",         // required, min: 3 chars, max: 100 chars
  "number": "string",         // optional, max: 20 chars
  "complement": "string",     // optional, max: 50 chars
  "neighborhood": "string",   // required, min: 2 chars, max: 50 chars
  "city": "string",           // required, min: 2 chars, max: 50 chars
  "zipCode": "string"         // required, min: 8 chars, max: 10 chars (formato: 01414-000)
}
```

**Response 201 - Created:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao",
  "role": "CLIENT",
  "createdAt": "2025-10-04T10:30:00Z"
}
```

**Response 400 - Validation Error:**
```json
{
  "type": "https://api.fiap.com.br/problems/invalid-field",
  "title": "Campo Inválido",
  "status": 400,
  "detail": "Email com formato inválido",
  "instance": "/api/v1/users",
  "timestamp": "2025-10-04T10:30:00Z",
  "fieldName": "email"
}
```

**Response 409 - Conflict:**
```json
{
  "type": "https://api.fiap.com.br/problems/conflict",
  "title": "Conflito",
  "status": 409,
  "detail": "Usuário com login 'joao' já existe",
  "instance": "/api/v1/users",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
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

---

#### POST /api/v1/users/owner

Cria um novo usuário com role OWNER (apenas ADMIN).

**Endpoint**: `POST /api/v1/users/owner`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN` only  
**Content-Type**: `application/json`

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "login": "string",
  "password": "string"
}
```

**Response 201 - Created:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "name": "Maria Restaurante",
  "email": "maria@restaurant.com",
  "login": "maria_owner",
  "role": "OWNER",
  "createdAt": "2025-10-04T10:30:00Z"
}
```

**Response 403 - Forbidden:**
```json
{
  "type": "https://api.fiap.com.br/problems/forbidden",
  "title": "Acesso Negado",
  "status": 403,
  "detail": "Apenas administradores podem criar usuários OWNER",
  "instance": "/api/v1/users/owner",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/users/owner \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "Maria Restaurante",
    "email": "maria@restaurant.com",
    "login": "maria_owner",
    "password": "senha123"
  }'
```

---

#### GET /api/v1/users/{id}

Busca um usuário por ID.

**Endpoint**: `GET /api/v1/users/{id}`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN`, `OWNER`

**Path Parameters:**
- `id` (UUID) - ID do usuário

**Response 200 - Success:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao",
  "role": "CLIENT",
  "createdAt": "2025-10-04T10:30:00Z"
}
```

**Response 404 - Not Found:**
```json
{
  "type": "https://api.fiap.com.br/problems/not-found",
  "title": "Recurso Não Encontrado",
  "status": 404,
  "detail": "Usuário com ID '123e4567...' não foi encontrado",
  "instance": "/api/v1/users/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer {token}"
```

---

#### GET /api/v1/users/by-name

Busca usuários por nome (busca parcial, case-insensitive).

**Endpoint**: `GET /api/v1/users/by-name`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN`, `OWNER`

**Query Parameters:**
- `name` (string) - Nome ou parte do nome do usuário

**Response 200 - Success:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao",
    "role": "CLIENT",
    "createdAt": "2025-10-04T10:30:00Z"
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "name": "João Pedro",
    "email": "joaop@example.com",
    "login": "joaop",
    "role": "CLIENT",
    "createdAt": "2025-10-04T10:31:00Z"
  }
]
```

**Response 200 - Empty:**
```json
[]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/users/by-name?name=João" \
  -H "Authorization: Bearer {token}"
```

---

#### PUT /api/v1/users/{id}

Atualiza dados de um usuário.

**Endpoint**: `PUT /api/v1/users/{id}`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN`, `OWNER`, `CLIENT`  
**Content-Type**: `application/json`

**Path Parameters:**
- `id` (UUID) - ID do usuário

**Request Body:**
```json
{
  "name": "string",       // optional, min: 3 chars
  "email": "string"       // optional, valid email format
}
```

**Response 200 - Success:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva Updated",
  "email": "joao.new@example.com",
  "login": "joao",
  "role": "CLIENT",
  "createdAt": "2025-10-04T10:30:00Z"
}
```

**Response 422 - Business Rule Violation:**
```json
{
  "type": "https://api.fiap.com.br/problems/business-rule-violation",
  "title": "Violação de Regra de Negócio",
  "status": 422,
  "detail": "Usuário não tem permissão para alterar este recurso",
  "instance": "/api/v1/users/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "João Silva Updated",
    "email": "joao.new@example.com"
  }'
```

---

#### PATCH /api/v1/users/password

Atualiza a própria senha do usuário autenticado.

**Endpoint**: `PATCH /api/v1/users/password`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN`, `OWNER`, `CLIENT` (qualquer usuário autenticado pode alterar a própria senha)  
**Content-Type**: `application/json`

**Request Body:**
```json
{
  "oldPassword": "string",    // required
  "newPassword": "string"     // required, min: 6 chars
}
```

**Response 200 - Success:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao",
  "role": "CLIENT",
  "createdAt": "2025-10-04T10:30:00Z"
}
```

**Response 401 - Unauthorized:**
```json
{
  "type": "https://api.fiap.com.br/problems/unauthorized",
  "title": "Não Autorizado",
  "status": 401,
  "detail": "Senha atual incorreta",
  "instance": "/api/v1/users/password",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "oldPassword": "senha123",
    "newPassword": "novaSenha456"
  }'
```

---

#### DELETE /api/v1/users/{id}

Deleta um usuário (apenas ADMIN).

**Endpoint**: `DELETE /api/v1/users/{id}`  
**Autenticação**: ✅ JWT Required  
**Autorização**: `ADMIN` only

**Path Parameters:**
- `id` (UUID) - ID do usuário

**Response 204 - No Content:**
```
(empty body)
```

**Response 404 - Not Found:**
```json
{
  "type": "https://api.fiap.com.br/problems/not-found",
  "title": "Recurso Não Encontrado",
  "status": 404,
  "detail": "Usuário com ID '123e4567...' não foi encontrado",
  "instance": "/api/v1/users/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer {token}"
```



---

## 📦 Schemas

### LoginRequest

```json
{
  "login": "string",      // required, min: 3 chars
  "password": "string"    // required, min: 6 chars
}
```

### LoginResponse

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600, // Tempo de expiração em segundos (ex: 1 hora)
  "refreshToken": "optional.refresh.token.if.used...", // Opcional, se usar o padrão Refresh Token
  "userProfile": { // Opcional: Dados básicos do usuário para o frontend
    "id": 123,
    "email": "usuario@exemplo.com",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  }
}
```

### UserCreateRequest

```json
{
  "name": "string",       // required, min: 3 chars, max: 100 chars
  "email": "string",      // required, valid email format
  "login": "string",      // required, min: 3 chars, unique
  "password": "string"    // required, min: 6 chars
}
```

### UserUpdateRequest

```json
{
  "name": "string",       // optional, min: 3 chars, max: 100 chars
  "email": "string"       // optional, valid email format
}
```

### UpdatePasswordRequest

```json
{
  "oldPassword": "string",    // required
  "newPassword": "string"     // required, min: 6 chars
}
```

### UserResponse

```json
{
  "id": "uuid",           // UUID v4
  "name": "string",
  "email": "string",
  "login": "string",
  "role": "string",       // ADMIN | OWNER | CLIENT
  "createdAt": "string"   // ISO-8601 DateTime
}
```

### ProblemDetail (RFC 7807)

```json
{
  "type": "string",       // URI identifying the problem type
  "title": "string",      // Short, human-readable summary
  "status": 400,          // HTTP status code
  "detail": "string",     // Specific explanation
  "instance": "string",   // URI reference to specific occurrence
  "timestamp": "string",  // ISO-8601 DateTime
  "fieldName": "string"   // Optional: field that caused the error
}
```

---

## ⚠️ Error Handling

A API segue a **RFC 7807 - Problem Details for HTTP APIs**.

### HTTP Status Codes

| Status | Descrição | Quando Usar |
|--------|-----------|-------------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Operação bem-sucedida sem corpo de resposta |
| 400 | Bad Request | Dados inválidos na requisição |
| 401 | Unauthorized | Autenticação falhou ou token inválido |
| 403 | Forbidden | Usuário autenticado mas sem permissão |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Conflito de dados (ex: duplicação) |
| 422 | Unprocessable Entity | Violação de regra de negócio |
| 500 | Internal Server Error | Erro interno não tratado |

### Error Response Examples

#### 400 - Validation Error
```json
{
  "type": "https://api.fiap.com.br/problems/invalid-field",
  "title": "Campo Inválido",
  "status": 400,
  "detail": "Email com formato inválido",
  "instance": "/api/v1/users",
  "timestamp": "2025-10-04T10:30:00Z",
  "fieldName": "email"
}
```

#### 401 - Unauthorized
```json
{
  "type": "https://api.fiap.com.br/problems/unauthorized",
  "title": "Não Autorizado",
  "status": 401,
  "detail": "Token JWT inválido ou expirado",
  "instance": "/api/v1/users/123",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

#### 404 - Not Found
```json
{
  "type": "https://api.fiap.com.br/problems/not-found",
  "title": "Recurso Não Encontrado",
  "status": 404,
  "detail": "Usuário com ID '123e4567...' não foi encontrado",
  "instance": "/api/v1/users/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

#### 409 - Conflict
```json
{
  "type": "https://api.fiap.com.br/problems/conflict",
  "title": "Conflito",
  "status": 409,
  "detail": "Usuário com login 'joao' já existe",
  "instance": "/api/v1/users",
  "timestamp": "2025-10-04T10:30:00Z"
}
```

---

## 📖 Swagger/OpenAPI

### Acessar Swagger UI

🚧 **Em desenvolvimento** - Swagger UI será disponibilizado em breve.

**URL Planejada**: `http://localhost:8080/swagger-ui.html`

### OpenAPI Specification

🚧 **Em desenvolvimento** - Especificação OpenAPI 3.0 será gerada automaticamente.

**URL Planejada**: `http://localhost:8080/v3/api-docs`

### Como Habilitar (Futuro)

1. Adicionar dependência no `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

2. Configurar no `application.properties`:
```properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

3. Acessar: `http://localhost:8080/swagger-ui.html`

---

## 🔧 Configuração da API

### Variáveis de Ambiente

```bash
# Server
SERVER_PORT=8080

# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/techchallenge
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT
JWT_SECRET_KEY=your-secret-key-minimum-256-bits
JWT_EXPIRATION_MINUTE=60
JWT_ISSUER=tech-challenge-fase-01

# CORS (se necessário)
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

### Headers Recomendados

**Request Headers:**
```
Content-Type: application/json
Accept: application/json
Authorization: Bearer {token}
```

**Response Headers:**
```
Content-Type: application/json
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

---

## 📊 Rate Limiting

🚧 **Planejado para próxima versão**

- Limite de requisições por IP
- Limite de requisições por usuário
- Headers informativos (X-RateLimit-*)

---

## 🧪 Testando a API

### Ferramentas Recomendadas

- **Postman** - [Collection planejada]
- **Insomnia** - [Workspace planejado]
- **cURL** - Exemplos fornecidos em cada endpoint
- **HTTPie** - Alternativa ao cURL

### Collection Postman (Futuro)

🚧 Collection Postman será disponibilizada com:
- Todos os endpoints
- Variáveis de ambiente
- Exemplos de requests
- Testes automatizados

---

## 📞 Suporte

### Reportar Problemas

- GitHub Issues: [Link do repositório]
- Email: [suporte@fiap.com.br]

### Changelog

Ver [CHANGELOG.md](../CHANGELOG.md) para histórico de mudanças.

---

## 📚 Documentação Relacionada

- **[RFC 7807 Exception Handling](RFC-7807-EXCEPTION-HANDLING.md)** - Detalhes sobre erros
- **[Infrastructure Layer](INFRASTRUCTURE-LAYER.md)** - Arquitetura dos controllers
- **[Security Documentation](PROJECT-VALIDATION-REPORT.md#-validação-de-segurança)** - Detalhes de segurança

---

**Desenvolvido pela Equipe 3 - FIAP**  
**Tech Challenge - Fase 01**  
**Versão**: 1.0.0  
**Última Atualização**: 04/10/2025
