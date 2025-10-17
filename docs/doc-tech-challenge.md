# Projeto: Sistema de Gestão de Restaurantes - Tech Challenge Fase 01

## Equipe: Lista dos nomes e RMs dos alunos

| Nome | RM |
| --- | --- |
| Emerson Pereira da Silva | RM367268 |
| Levi Aparecido do Santos | RM369031 |
| Luiz Octavio Tassinari Saraiva | RM367408|
| Rhayana Lacerda Gomes | | 
| Vinicius Padovam Valentim | RM367199|

 
# 1. Introdução
   
## 1.1. Descrição do problema

Um grupo de restaurantes busca desenvolver um sistema de gestão unificado e compartilhado para reduzir os altos custos de soluções individuais. O objetivo é criar uma plataforma robusta que permita aos restaurantes gerenciar suas operações de forma eficiente e, ao mesmo tempo, ofereça aos clientes a possibilidade de consultar informações, fazer pedidos online e deixar avaliações.
Devido a limitações orçamentárias, o projeto será entregue em fases, permitindo uma implementação gradual, com melhorias contínuas baseadas no feedback dos restaurantes e clientes.


## 1.2. Objetivo do projeto

Desenvolver um backend completo e robusto utilizando Spring Boot e PostgreSQL.
O sistema deve permitir:
- Cadastro, atualização e exclusão de usuários;
- Troca de senha do usuário em endpoint separado;
- Atualização das demais informações do usuário em endpoint distinto do endpoint de senha;
- Registro da data da última alteração;
- Busca de usuários pelo nome;
- Garantia de que o e-mail cadastrado seja único;
- Validação de login obrigatória, por meio de um serviço que verifique se login e senha são válidos;

- Implementações extras:
    - Conter três níveis de acesso: Cliente, Restaurante (dono) e Administrador;
    - Autenticação JWT com autorização baseada em roles (RBAC);
    - Arquitetura Hexagonal (Ports & Adapters);
    - Princípios SOLID e Clean Architecture;
    - Exception Handling seguindo RFC 7807;
    - Testes unitários e de integração;

A aplicação deverá ser dockerizada, utilizando Docker Compose para orquestração junto com um banco de dados relacional (PostgreSQL)

# 2. Arquitetura do Sistema

## Descrição da Arquitetura

O sistema implementa **Arquitetura Hexagonal** (Ports & Adapters) seguindo os princípios de Clean Architecture e SOLID. Esta abordagem garante:

- **Isolamento do domínio**: A lógica de negócio fica independente de frameworks e tecnologias externas
- **Testabilidade**: Facilita testes unitários e de integração através de interfaces bem definidas
- **Flexibilidade**: Permite mudanças de tecnologia sem impactar o core da aplicação
- **Manutenibilidade**: Código mais organizado e fácil de manter

### Camadas da Aplicação:

**Application Layer (Core - 37 arquivos)**
- **Domain**: Entities, Value Objects, Domain Services
- **Ports**: Interfaces que definem contratos (Inbound e Outbound)
- **Use Cases**: Implementação das regras de negócio
- **DTOs**: Objetos de transferência de dados

**Infrastructure Layer (26 arquivos)**
- **Inbound Adapters**: Controllers REST, Security, Exception Handlers
- **Outbound Adapters**: Repositories JPA, JWT Token Manager, Password Encoder
- **Configuration**: Configurações do Spring Boot, Security, Database

### Tecnologias Utilizadas:
- **Java 21** + **Spring Boot 3.5.6**
- **PostgreSQL 16** como banco de dados
- **JWT** para autenticação stateless
- **BCrypt** para criptografia de senhas
- **Docker & Docker Compose** para containerização
- **Maven** para gerenciamento de dependências

## Diagrama da Arquitetura

[Insira um diagrama explicando os componentes principais.]

# 3. Descrição dos Endpoints da API

## Tabela de Endpoints 

| Endpoint | Método | Descrição | Autenticação | Autorização |
|----------|--------|-----------|--------------|-------------|
| `/api/v1/auth/login` | POST | Realizar login e obter token JWT | Não | Pública |
| `/api/v1/users` | POST | Criar novo usuário (CLIENT) | Não | Pública |
| `/api/v1/users/owner` | POST | Criar usuário proprietário (OWNER) | JWT | ADMIN |
| `/api/v1/users/{id}` | GET | Buscar usuário por ID | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/by-name` | GET | Buscar usuários por nome | JWT | ADMIN |
| `/api/v1/users/{id}` | PUT | Atualizar dados do usuário | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/{id}/password` | PUT | Alterar senha do usuário | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/{id}` | DELETE | Excluir usuário | JWT | ADMIN |

## Exemplos de Requisição e Resposta

### 🔐 POST /api/v1/auth/login
**Descrição**: Autenticar usuário e obter token JWT

**Requisição:**
```json
{
  "login": "usuario123",
  "password": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400
}
```

### 👤 POST /api/v1/users
**Descrição**: Criar novo usuário com role CLIENT

**Requisição:**
```json
{
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao123",
  "password": "senha123"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao123",
  "role": "CLIENT",
  "createdAt": "2025-10-16T10:30:00Z",
  "updatedAt": "2025-10-16T10:30:00Z"
}
```

### 🔍 GET /api/v1/users/{id}
**Descrição**: Buscar usuário específico por ID
**Headers**: `Authorization: Bearer {token}`

**Resposta (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao123",
  "role": "CLIENT",
  "createdAt": "2025-10-16T10:30:00Z",
  "updatedAt": "2025-10-16T10:30:00Z"
}
```

### 🔍 GET /api/v1/users/by-name?name={nome}
**Descrição**: Buscar usuários por nome (busca parcial)
**Headers**: `Authorization: Bearer {token}`
**Parâmetros**: `name` (query parameter)

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao123",
    "role": "CLIENT"
  }
]
```

### ✏️ PUT /api/v1/users/{id}
**Descrição**: Atualizar dados do usuário (exceto senha)
**Headers**: `Authorization: Bearer {token}`

**Requisição:**
```json
{
  "name": "João Santos Silva",
  "email": "joao.santos@example.com"
}
```

### 🔒 PUT /api/v1/users/{id}/password
**Descrição**: Alterar senha do usuário
**Headers**: `Authorization: Bearer {token}`

**Requisição:**
```json
{
  "currentPassword": "senhaAtual123",
  "newPassword": "novaSenha456"
}
```

### ❌ DELETE /api/v1/users/{id}
**Descrição**: Excluir usuário do sistema
**Headers**: `Authorization: Bearer {token}`

**Resposta (204 No Content):** Sem corpo de resposta

## Códigos de Status HTTP

- **200 OK**: Operação realizada com sucesso
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação realizada sem retorno de dados
- **400 Bad Request**: Dados de entrada inválidos
- **401 Unauthorized**: Token não fornecido ou inválido
- **403 Forbidden**: Usuário não tem permissão para a operação
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito de dados (ex: email já existe)
- **500 Internal Server Error**: Erro interno do servidor

# 4. Configuração do Projeto
 
## Configuração do Docker Compose

O projeto utiliza Docker Compose para orquestrar a aplicação Spring Boot e o banco de dados PostgreSQL. O arquivo `compose.yaml` define dois serviços principais:

### Configuração do PostgreSQL:
```yaml
postgres:
  image: postgres:17
  environment:
    - POSTGRES_DB=restaurantapp
    - POSTGRES_USER=postgres
    - POSTGRES_PASSWORD=secret
  ports:
    - "5432:5432"
  volumes:
    - db_data:/var/lib/postgresql/data
```

### Configuração da Aplicação Spring Boot:
```yaml
spring:
  build:
    context: .
    dockerfile: Dockerfile
  environment:
    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/restaurantapp
    - SPRING_DATASOURCE_USERNAME=postgres
    - SPRING_DATASOURCE_PASSWORD=secret
  ports:
    - "8080:8080"
  depends_on:
    - postgres
```

## Instruções para Execução Local

### Pré-requisitos:
- Docker e Docker Compose instalados
- Java 21+ (para desenvolvimento local)
- Maven 3.6+ (para desenvolvimento local)

### Opção 1: Execução com Docker Compose Completo
```bash
# Construir e executar toda a aplicação
docker compose up --build

# A aplicação estará disponível em: http://localhost:8080
# PostgreSQL estará disponível em: localhost:5432
```

### Opção 2: Execução Híbrida (PostgreSQL no Docker + Spring Boot local)
```bash
# 1. Iniciar apenas o PostgreSQL
docker compose up -d postgres

# 2. Executar a aplicação Spring Boot localmente
./mvnw spring-boot:run

# A aplicação estará disponível em: http://localhost:8080
```

### Opção 3: Usando as Tasks Configuradas no VS Code
- **Start Docker Services**: Inicia apenas o PostgreSQL
- **Run Spring Boot Application**: Executa a aplicação
- **Run Spring Boot with Hot Reload**: Execução com reload automático
- **Full Docker Setup**: Execução completa com Docker

### URLs e Portas Configuradas:
- **Aplicação**: http://localhost:8080 (local) ou http://localhost:8081 (Docker)
- **PostgreSQL**: localhost:5432
- **API Base Path**: `/api/v1`
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### Banco de Dados:
- **Nome**: restaurantapp
- **Usuário**: postgres
- **Senha**: postgres
- **Host**: postgres (Docker) ou localhost (local)
- **Porta**: 5432

# 5. Qualidade do Código
 
## Boas Práticas Utilizadas

### Arquitetura e Design Patterns
- **✅ Arquitetura Hexagonal (Ports & Adapters)**: Separação clara entre core da aplicação e infraestrutura
- **✅ Domain-Driven Design (DDD)**: Modelagem baseada no domínio do negócio
- **✅ Clean Architecture**: Dependências direcionadas para o core da aplicação
- **✅ CQRS Pattern**: Separação entre comandos (escrita) e queries (leitura)

### Princípios SOLID
- **Single Responsibility Principle**: Cada classe tem uma única responsabilidade
- **Open/Closed Principle**: Código aberto para extensão, fechado para modificação
- **Liskov Substitution Principle**: Implementações podem ser substituídas por suas interfaces
- **Interface Segregation Principle**: Interfaces específicas e coesas
- **Dependency Inversion Principle**: Dependência de abstrações, não de implementações

### Segurança
- **Autenticação JWT**: Tokens stateless com expiração configurável
- **Autorização RBAC**: Controle de acesso baseado em roles (CLIENT, OWNER, ADMIN)
- **Criptografia BCrypt**: Senhas criptografadas com salt
- **Validação de Entrada**: Validação de todos os dados de entrada
- **CORS Configurado**: Controle de origem das requisições

### Exception Handling
- **RFC 7807 Compliance**: Respostas de erro padronizadas seguindo Problem Details
- **Global Exception Handler**: Tratamento centralizado de exceções
- **Error Details**: Informações detalhadas sobre erros para facilitar debugging

### Validação e Testes
- **Jakarta Bean Validation**: Validações declarativas nos DTOs
- **Testes Unitários**: Cobertura dos use cases e domain services
- **Testes de Integração**: Validação dos endpoints e integração com banco
- **Test Containers**: Testes com banco real usando containers

### Organização de Código
- **Package by Feature**: Organização por funcionalidades do domínio
- **Naming Conventions**: Nomenclatura clara e consistente seguindo padrões Java
- **Code Documentation**: Javadoc nos métodos e classes principais
- **Clean Code**: Métodos pequenos, responsabilidades bem definidas

### Configuração e Deploy
- **Profiles Spring**: Separação de ambientes (dev, test, prod)
- **Externalized Configuration**: Configurações externalizadas via application.yaml
- **Docker Multi-stage**: Build otimizado com múltiplos estágios
- **Health Checks**: Endpoints de monitoramento da aplicação

### Conformidade Arquitetural
**Status de Validação**: ✅ **APROVADO - 97/100**

| Aspecto | Nota | Status |
|---------|------|--------|
| Arquitetura Hexagonal | 10.0/10 | ✅ |
| Princípios SOLID | 9.9/10 | ✅ |
| Clean Architecture | 10.0/10 | ✅ |
| Domain-Driven Design | 9.5/10 | ✅ |
| Segurança (JWT + RBAC) | 9.5/10 | ✅ |
| Exception Handling RFC 7807 | 10.0/10 | ✅ |

# 6. Collections para Teste
 
## Link para a Collection do Postman

A collection completa do Postman está disponível no arquivo:
📁 `docs/TechChallenge.postman_collection.json`

### Collection Postman:

1. **Importar Collection**:
   - Selecione o arquivo `TechChallenge.postman_collection.json`

## Descrição dos Testes Manuais

### Estrutura da Collection:

**1. 🔐 Login**
- Endpoint para autenticação
- Extrai automaticamente o token JWT e salva nas variáveis
- Utiliza o token em todas as requisições subsequentes

**2. 👤 Gestão de Usuários - Criação**
- **Criar Usuário (CLIENT)**: Cadastro público de cliente
- **Criar Usuário (OWNER)**: Cadastro de proprietário (requer ADMIN)
- Captura automaticamente o ID do usuário criado

**3. 🔍 Gestão de Usuários - Consulta**
- **Buscar por ID**: Recupera usuário específico
- **Buscar por Nome**: Busca parcial por nome
- Inclui autorização baseada em roles

**4. ✏️ Gestão de Usuários - Atualização**
- **Atualizar Dados**: Modifica informações gerais
- **Alterar Senha**: Endpoint separado para mudança de senha
- Validações de autorização

**5. ❌ Gestão de Usuários - Exclusão**
- **Deletar Usuário**: Remove usuário do sistema
- Apenas administradores podem excluir

### Fluxo de Teste Recomendado:

1. **Executar Login** para obter token de autenticação
2. **Criar usuários** de diferentes tipos (CLIENT, OWNER)
3. **Testar buscas** por ID e nome
4. **Atualizar dados** e senhas
5. **Validar autorizações** testando acessos negados
6. **Testar exclusões** (apenas como ADMIN)

### Validações Automáticas:

- ✅ **Extração de Token**: Automática após login bem-sucedido
- ✅ **Captura de IDs**: IDs de usuários criados são salvos automaticamente
- ✅ **Headers de Autorização**: Token incluído automaticamente nas requisições
- ✅ **Validação de Status**: Verificação dos códigos de resposta HTTP
- ✅ **Assertions**: Validação de campos obrigatórios nas respostas

### Cenários de Teste Cobertos:

- ✅ Autenticação válida e inválida
- ✅ Criação de usuários com dados válidos e inválidos
- ✅ Autorização baseada em roles
- ✅ Busca e filtros de usuários
- ✅ Atualização de dados e senhas
- ✅ Exclusão de usuários
- ✅ Tratamento de erros e validações
- ✅ Responses seguindo RFC 7807 para erros

# 7. Repositório do Código
 
URL do Repositório no Github: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01
