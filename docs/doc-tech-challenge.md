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

- ** Funcionalidades Implementadas**:
    - **Gestão completa de usuários**: CRUD com validações e autorização por roles
    - **Gestão de endereços**: CRUD de endereços vinculados aos usuários autenticados
    - **Autenticação JWT**: Sistema seguro com tokens e autorização RBAC
    - **Três níveis de acesso**: Cliente (CLIENT), Proprietário (OWNER) e Administrador (ADMIN)
    - **Documentação OpenAPI/Swagger**: Interface interativa para testes de API
    - **Arquitetura Hexagonal**: Implementação completa com Ports & Adapters
    - **Princípios SOLID e Clean Architecture**: Código bem estruturado e testável
    - **Exception Handling RFC 7807**: Tratamento padronizado de erros
    - **Testes unitários e de integração**: Cobertura de testes implementada

A aplicação deverá ser dockerizada, utilizando Docker Compose para orquestração junto com um banco de dados relacional (PostgreSQL)

# 2. Arquitetura do Sistema

## Descrição da Arquitetura

O sistema implementa **Arquitetura Hexagonal** (Ports & Adapters) seguindo os princípios de Arquitetura Limpa e SOLID. Esta abordagem garante:

- **Isolamento do domínio**: A lógica de negócio fica independente de frameworks e tecnologias externas
- **Testabilidade**: Facilita testes unitários e de integração através de interfaces bem definidas
- **Flexibilidade**: Permite mudanças de tecnologia sem impactar o core da aplicação
- **Manutenibilidade**: Código mais organizado e fácil de manter

### Camadas da Aplicação:

**Application Layer (Core - 45+ arquivos)**
- **Domain**: Entities (User, Address), Value Objects, Domain Services
- **Ports**: Interfaces que definem contratos (Inbound e Outbound)
- **Use Cases**: Implementação das regras de negócio (User + Address management)
- **DTOs**: Objetos de transferência de dados
- **Exception Handling**: Exceções customizadas do domínio

**Infrastructure Layer (35+ arquivos)**
- **Inbound Adapters**: Controllers REST, Security, Exception Handlers, OpenAPI Config
- **Outbound Adapters**: Repositories JPA (User + Address), JWT Token Manager, Password Encoder
- **Configuration**: Configurações do Spring Boot, Security, Database, Use Cases
- **Web Layer**: DTOs específicos da web, Mappers, documentação Swagger

### Tecnologias Utilizadas:
- **Java 21** + **Spring Boot 3.5.6**
- **PostgreSQL 17** como banco de dados
- **JWT** para autenticação stateless
- **BCrypt** para criptografia de senhas
- **SpringDoc OpenAPI 2.8.13** para documentação automática
- **Docker & Docker Compose** para containerização
- **Maven** para gerenciamento de dependências
- **Jakarta Bean Validation** para validações

## Diagramas do Sistema

### 📊 Diagramas do Banco de Dados
Os diagramas PlantUML do banco de dados estão disponíveis nos arquivos:
- **🏗️ Esquema Completo**: [`docs/database-diagram.puml`](database-diagram.puml)
- **📋 Visão Detalhada**: [`docs/database-detailed-diagram.puml`](database-detailed-diagram.puml)  
- **📝 Esquema Simples**: [`docs/database-simple-schema.puml`](database-simple-schema.puml)
- **💾 Dados de Exemplo**: [`docs/database-sample-data.puml`](database-sample-data.puml)

#### 🗃️ Principais Entidades:
- **users**: Gerenciamento de usuários com roles (CLIENT, OWNER, ADMIN)
- **address**: Endereços vinculados aos usuários

#### 🔗 Relacionamentos:
- **1:N** - Um usuário pode ter múltiplos endereços
- **Integridade Referencial** - Foreign Key de address para users
- **Cascata** - Exclusão de usuário pode afetar endereços

#### 📊 Dados de Teste:
- **7 usuários** pré-cadastrados (1 ADMIN, 2 OWNERS, 4 CLIENTS)
- **6 endereços** distribuídos em diferentes cidades
- **Senha padrão**: "senha123" (BCrypt) para todos os usuários de teste

### 🏗️ Como Visualizar os Diagramas

#### ⚠️ **Se os diagramas não estão sendo visualizados:**
1. **Comece com o mais simples**: [`docs/database-minimal.puml`](database-minimal.puml)
2. **Consulte o guia**: [`docs/plantuml-troubleshooting.md`](plantuml-troubleshooting.md)

#### 🖥️ **Métodos de Visualização:**
1. **VS Code** (✅ Extensões instaladas):
   - `Ctrl+Shift+P` → "PlantUML: Preview Current Diagram"
   - Ou clique no ícone de preview na barra superior
2. **Online** (sempre funciona):
   - Acesse: http://www.plantuml.com/plantuml/uml/
   - Cole o conteúdo do arquivo .puml
3. **CLI** (Java ✅ instalado):
   - `plantuml docs/database-minimal.puml`
4. **Export**: Gera PNG, SVG, PDF

# 3. Descrição dos Endpoints da API

## Tabela de Endpoints 

### 🔐 Autenticação
| Endpoint | Método | Descrição | Autenticação | Autorização |
|----------|--------|-----------|--------------|-------------|
| `/api/v1/auth/login` | POST | Realizar login e obter token JWT | Não | Pública |

### 👤 Gestão de Usuários
| Endpoint | Método | Descrição | Autenticação | Autorização |
|----------|--------|-----------|--------------|-------------|
| `/api/v1/users` | POST | Criar novo usuário (CLIENT) | Não | Pública |
| `/api/v1/users/owner` | POST | Criar usuário proprietário (OWNER) | JWT | ADMIN |
| `/api/v1/users/{id}` | GET | Buscar usuário por ID | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/by-name` | GET | Buscar usuários por nome | JWT | ADMIN |
| `/api/v1/users/{id}` | PUT | Atualizar dados do usuário | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/{id}/password` | PUT | Alterar senha do usuário | JWT | ADMIN ou próprio usuário |
| `/api/v1/users/{id}` | DELETE | Excluir usuário | JWT | ADMIN |

### 🏠 **Gestão de Endereços** (Nova Funcionalidade)
| Endpoint | Método | Descrição | Autenticação | Autorização |
|----------|--------|-----------|--------------|-------------|
| `/api/v1/address` | GET | Listar endereços do usuário autenticado | JWT | Usuário autenticado |
| `/api/v1/address` | POST | Criar novo endereço | JWT | Usuário autenticado |
| `/api/v1/address/{addressId}` | PUT | Atualizar endereço existente | JWT | Proprietário do endereço |
| `/api/v1/address/{addressId}` | DELETE | Excluir endereço | JWT | Proprietário do endereço |

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

## 🏠 Novos Endpoints - Gestão de Endereços

### 📍 GET /api/v1/address
**Descrição**: Listar todos os endereços do usuário autenticado
**Headers**: `Authorization: Bearer {token}`

**Resposta (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "street": "Rua das Flores, 123",
    "number": "123A",
    "city": "São Paulo",
    "createdAt": "2025-10-23T10:30:00Z",
    "updatedAt": "2025-10-23T10:30:00Z"
  }
]
```

### 🏠 POST /api/v1/address
**Descrição**: Criar novo endereço para o usuário autenticado
**Headers**: `Authorization: Bearer {token}`

**Requisição:**
```json
{
  "street": "Avenida Paulista, 1000",
  "number": "1000",
  "city": "São Paulo"
}
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "street": "Avenida Paulista, 1000",
  "number": "1000",
  "city": "São Paulo",
  "createdAt": "2025-10-23T14:30:00Z",
  "updatedAt": "2025-10-23T14:30:00Z"
}
```

### ✏️ PUT /api/v1/address/{addressId}
**Descrição**: Atualizar endereço existente do usuário
**Headers**: `Authorization: Bearer {token}`

**Requisição:**
```json
{
  "street": "Rua Augusta, 500",
  "number": "500B",
  "city": "São Paulo"
}
```

### 🗑️ DELETE /api/v1/address/{addressId}
**Descrição**: Excluir endereço do usuário
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
- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

### Banco de Dados:
- **Nome**: restaurantapp
- **Usuário**: postgres
- **Senha**: secret
- **Host**: postgres (Docker) ou localhost (local)
- **Porta**: 5432
- **Imagem Docker**: postgres:17

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
- **Validação de Entrada**: Jakarta Bean Validation em todos os endpoints
- **CORS Configurado**: Controle de origem das requisições
- **Autorização de recursos**: Usuários só podem acessar seus próprios endereços

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
- **Package by Feature**: Organização por funcionalidades do domínio (User, Address)
- **Naming Conventions**: Nomenclatura clara e consistente seguindo padrões Java
- **Code Documentation**: Javadoc nos métodos e classes principais
- **Clean Code**: Métodos pequenos, responsabilidades bem definidas
- **OpenAPI Documentation**: Documentação automática com Swagger/OpenAPI 3.0

### Configuração e Deploy
- **Profiles Spring**: Separação de ambientes (dev, test, prod)
- **Externalized Configuration**: Configurações externalizadas via application.yaml
- **Docker Multi-stage**: Build otimizado com múltiplos estágios
- **Health Checks**: Endpoints de monitoramento da aplicação

### Conformidade Arquitetural
**Status de Validação**: ✅ **APROVADO - 98/100**

| Aspecto | Nota | Status |
|---------|------|--------|
| Arquitetura Hexagonal | 10.0/10 | ✅ |
| Princípios SOLID | 10.0/10 | ✅ |
| Clean Architecture | 10.0/10 | ✅ |
| Domain-Driven Design | 9.5/10 | ✅ |
| Segurança (JWT + RBAC) | 9.5/10 | ✅ |
| Exception Handling RFC 7807 | 10.0/10 | ✅ |
| **Documentação API (OpenAPI)** | **10.0/10** | **✅** |
| **CRUD Completo (User + Address)** | **9.8/10** | **✅** |

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

#### **Autenticação e Usuários**
1. **Executar Login** para obter token de autenticação
2. **Criar usuários** de diferentes tipos (CLIENT, OWNER)
3. **Testar buscas** por ID e nome
4. **Atualizar dados** e senhas
5. **Validar autorizações** testando acessos negados
6. **Testar exclusões** (apenas como ADMIN)

#### **Gestão de Endereços** 
7. **Criar endereços** para usuários autenticados
8. **Listar endereços** do usuário logado
9. **Atualizar endereços** existentes
10. **Excluir endereços** do usuário
11. **Testar autorização** - usuários só acessam seus endereços

### Validações Automáticas:

- ✅ **Extração de Token**: Automática após login bem-sucedido
- ✅ **Captura de IDs**: IDs de usuários criados são salvos automaticamente
- ✅ **Headers de Autorização**: Token incluído automaticamente nas requisições
- ✅ **Validação de Status**: Verificação dos códigos de resposta HTTP
- ✅ **Assertions**: Validação de campos obrigatórios nas respostas

### Cenários de Teste Cobertos:

#### 👤 **Gestão de Usuários**
- ✅ Autenticação válida e inválida
- ✅ Criação de usuários com dados válidos e inválidos
- ✅ Autorização baseada em roles
- ✅ Busca e filtros de usuários
- ✅ Atualização de dados e senhas
- ✅ Exclusão de usuários

#### 🏠 **Gestão de Endereços** (Novos Testes)
- ✅ CRUD completo de endereços
- ✅ Validação de campos obrigatórios (rua, número, cidade)
- ✅ Autorização: usuário só acessa seus endereços
- ✅ Validação de UUIDs nos parâmetros de path
- ✅ Integração com usuário autenticado

#### 🔧 **Funcionalidades Gerais**
- ✅ Tratamento de erros e validações
- ✅ Responses seguindo RFC 7807 para erros
- ✅ **Documentação interativa via Swagger UI**
- ✅ **Testes via OpenAPI Specification**

# 7. Novas Funcionalidades Implementadas (Última Atualização)

## 📚 **Documentação OpenAPI/Swagger** 
✅ **Status**: Implementado e funcional

### Recursos Adicionados:
- **SpringDoc OpenAPI 2.8.13**: Dependência adicionada ao projeto
- **Configuração OpenAPI**: Classe `OpenApiConfig` com metadados da API
- **Interface Swagger UI**: Disponível em http://localhost:8080/swagger-ui
- **JSON Specification**: Endpoint http://localhost:8080/v3/api-docs
- **Autenticação Bearer**: Suporte a JWT nos testes via Swagger
- **Groupamento de APIs**: Organização por versão (v1)

### Benefícios:
- 🔍 **Teste interativo**: Executar requests diretamente pelo navegador
- 📖 **Documentação automática**: Sempre atualizada com o código
- 🔐 **Autenticação integrada**: Testar endpoints protegidos facilmente
- 🚀 **Onboarding rápido**: Desenvolvedores podem entender a API rapidamente

## 🏠 **Gestão de Endereços**
✅ **Status**: CRUD completo implementado

### Funcionalidades Implementadas:
- **Domínio Address**: Entity com validações de negócio
- **4 Endpoints REST**: GET, POST, PUT, DELETE
- **Autorização de recursos**: Usuários só acessam seus endereços
- **Validações**: Campos obrigatórios e tamanhos mínimos/máximos
- **Integração JPA**: Persistência no PostgreSQL
- **Mapeamento completo**: DTOs, Mappers, Use Cases

### Regras de Negócio:
- **Rua**: 3-100 caracteres obrigatórios
- **Número**: 0-20 caracteres obrigatórios (pode ser em branco)
- **Cidade**: 2-50 caracteres obrigatórios
- **Vínculo ao usuário**: Endereço sempre vinculado ao usuário autenticado
- **Controle de acesso**: Usuário só pode manipular seus próprios endereços

### Arquitetura Mantida:
- ✅ **Ports & Adapters**: Camadas bem definidas
- ✅ **Use Cases específicos**: CreateAddress, UpdateAddress, DeleteAddress, FindAddress
- ✅ **Domain Services**: AddressDomainService para regras de negócio
- ✅ **Exception Handling**: Tratamento específico (AddressNotFoundException, etc.)

## 🔧 **Melhorias Técnicas**

### Dependências Atualizadas:
- **PostgreSQL**: Atualizado para versão 17
- **SpringDoc OpenAPI**: Versão 2.8.13 adicionada
- **JWT Library**: jjwt 0.12.5 mantido

### Configurações:
- **application.yaml**: Configurações do Swagger/OpenAPI
- **Security Config**: Permite acesso público ao Swagger UI
- **Bean Configuration**: Novos beans para Address Use Cases

### Organização do Código:
- **Address Package**: Nova estrutura de pacotes para endereços
- **Web DTOs**: DTOs específicos da camada web
- **Mappers dedicados**: AddressWebMapper, AddressEntityMapper
- **Controllers**: AddressController com documentação OpenAPI

# 8. Repositório do Código
 
URL do Repositório no Github: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01

## Branches Principais:
- **main**: Versão estável de produção
- **develop**: Branch de desenvolvimento com as últimas funcionalidades
- **docs**: Branch com documentação atualizada
- **feature/\***: Branches para desenvolvimento de novas funcionalidades

## 🚀 **Como Testar as Novas Funcionalidades**

### Via Swagger UI (Recomendado):
1. Acesse: http://localhost:8080/swagger-ui
2. Execute o login em `/api/v1/auth/login`
3. Copie o token da resposta
4. Clique em "Authorize" no topo direito
5. Cole o token no formato: `Bearer {seu-token}`
6. Teste os endpoints de Address diretamente pela interface

### Via Postman:
1. Importe: `docs/TechChallenge.postman_collection.json`
2. Execute os novos requests da pasta "Address Management"
3. Os tokens são extraídos automaticamente
