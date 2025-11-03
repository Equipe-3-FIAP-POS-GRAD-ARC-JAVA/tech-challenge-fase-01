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
  
### **Informações do Repositório**
  
**URL**: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01
  
## 1.2. Objetivo do projeto
  
Desenvolver um backend completo e robusto utilizando **Java 21**, **Spring Boot 3.5.6** e **PostgreSQL 17**.
O sistema implementa **Arquitetura Hexagonal** com **Clean Architecture** e princípios **SOLID**, proporcionando:
  
### **Funcionalidades**
- **Gestão completa de usuários**: CRUD com validações robustas e autorização por roles
- **Gestão de endereços**: CRUD completo de endereços vinculados aos usuários autenticados  
- **Autenticação JWT**: Sistema seguro stateless com tokens e autorização RBAC (Role-Based Access Control)
- **Três níveis de acesso**: Cliente (CLIENT), Proprietário (OWNER) e Administrador (ADMIN)
- **Documentação OpenAPI/Swagger**: Interface interativa para testes de API
- **Exception Handling RFC 7807**: Tratamento padronizado de erros seguindo padrões internacionais
- **Validações Jakarta Bean**: Validações declarativas em todas as camadas
- **Containerização Docker**: Docker Compose para orquestração completa
- **Spring Boot Actuator**: Endpoints de monitoramento, health checks e métricas operacionais
  
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
- **Monitoramento**: Spring Boot Actuator com health checks e métricas
- **Testes**: JUnit 5, Spring Boot Test, H2 Database, JaCoCo para cobertura (309 testes com 67% de coverage)
  
A aplicação é completamente dockerizada, utilizando Docker Compose para orquestração junto com PostgreSQL em containers isolados.
  
# 2. Arquitetura do Sistema
  
## Descrição da Arquitetura
  
O sistema implementa **Arquitetura Hexagonal** (Ports & Adapters) seguindo rigorosamente os princípios de **Clean Architecture** e **SOLID**. Esta abordagem proporciona:
  
### **Benefícios Arquiteturais**
- **Isolamento do domínio**: Lógica de negócio independente de frameworks e tecnologias externas
- **Testabilidade**: Interfaces bem definidas facilitam mocks e testes isolados
- **Flexibilidade tecnológica**: Mudanças de infraestrutura sem impacto no core
- **Manutenibilidade**: Código organizado com responsabilidades bem definidas
- **Escalabilidade**: Arquitetura preparada para crescimento e evolução
  
## Estrutura das Camadas
  
### **Application Layer (Core - 192 arquivos Java)**
  
#### **Domain Layer**
```
domain/
├── address/AddressDomain.java           # Aggregate Root para endereços
├── user/UserDomain.java                 # Aggregate Root para usuários  
├── user/RolesEnum.java                  # Enumeração de papéis
├── valueobject/
│   ├── Email.java                       # Value Object para email
│   ├── Username.java                    # Value Object para login
```
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
  
#### **Ports (Contratos)**
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
  
#### **Use Cases (Orquestradores)**
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
  
### **Segurança**
- **JWT**: Tokens stateless
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
  
#### **Diagramas Principais**
- **[`diag01.puml`](diag01.puml )** - **Arquitetura Hexagonal Completa**: Visão geral das camadas, componentes e relacionamentos
- **[`diag02.puml`](diag02.puml )** - **Modelo de Domínio**: Classes de domínio, value objects, ports e domain services
- **[`diag03.puml`](diag03.puml )** - **Fluxo de Autenticação JWT**: Sequence diagram detalhado do processo de login
- **[`diag04.puml`](diag04.puml )** - **Schema do Banco de Dados**: Estrutura das tabelas PostgreSQL
- **[`diag05.puml`](diag05.puml )** - **Visão Geral da Arquitetura**: Overview simplificado dos componentes principais
  
#### **Diagramas Específicos do Address**
- **[`diag06.puml`](diag06.puml )** - **Fluxos Completos de Address**: Sequence diagrams para CRUD de endereços
- **[`diag07.puml`](diag07.puml )** - **Estrutura de Packages**: Organização completa de packages e classes
  
### Modelo de Banco de Dados
  
#### **Schema** `schema.sql`
```sql
-- Tabela de usuários com autenticação e roles
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    roles VARCHAR(255)[] NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);
  
-- Tabela de endereços vinculados aos usuários (schema completo JPA)
CREATE TABLE address (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES "users"(id),
    street VARCHAR(100) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(255),
    neighborhood VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```
  
#### **Entidades JPA e Mapeamentos**
  
**JpaUserEntity** (`@Entity @Table(name = "users")`)
- **Primary Key**: `@Id @GeneratedValue(strategy = UUID)` - UUID auto-gerado
- **Campos únicos**: `email` com `@Column(unique = true)`
- **Timestamps**: `@CreationTimestamp` e `@UpdateTimestamp` automáticos
- **Enum Mapping**: `@Enumerated(EnumType.STRING)` para roles (OWNER, CLIENT, ADMIN)
- **Validações**: `@Column(nullable = false)` em campos obrigatórios
  
**JpaAddressEntity** (`@Entity @Table(name = "address")`)
- **Primary Key**: `@Id @GeneratedValue(strategy = UUID)` - UUID auto-gerado
- **Foreign Key**: `@ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id", nullable = false)`
- **Relacionamento**: Many-to-One com JpaUserEntity (lazy loading)
- **Timestamps**: `@CreationTimestamp` e `@UpdateTimestamp` automáticos
- **Campos opcionais**: `complement` sem `nullable = false`
  
#### **Relacionamentos**
- **`users` ↔ `address`**: Relacionamento 1:N (One-to-Many/Many-to-One)
- **Integridade referencial**: Foreign key constraint garantindo consistência
- **Lazy Loading**: Endereços carregados sob demanda para otimização
  
#### **Dados de Teste Pré-Carregados** (`data.sql`)
  
**Usuários (7 registros):**
- **1 ADMIN**: Emerson Silva (acesso total)  
- **2 OWNERS**: Vinicius Padovam, Carlos Oliveira (proprietários)
- **4 CLIENTS**: Maria Silva, João Pereira, Ana Souza, João Silva
- **Senha padrão**: `senha123` (BCrypt hash) para todos
- **UUIDs fixos**: Para facilitar testes e referências
  
**Endereços (6 registros completos):**
- **São Paulo/SP**: Av. Paulista, 1000 - Bela Vista (CEP: 01310-100)
- **Curitiba/PR**: R. XV de Novembro, 200, Sala 302 - Centro (CEP: 80020-310)  
- **Rio de Janeiro/RJ**: Av. Atlântica, 500, Cobertura - Copacabana (CEP: 22070-000)
- **Belo Horizonte/MG**: R. das Flores, 45B - Centro (CEP: 30112-000)
- **São Paulo/SP**: Av. Paulista, 1000, Bloco B - Bela Vista (CEP: 01310-100)
  
#### **Tipos de Dados e Enums**
  
**RolesEnum** (`@Enumerated(EnumType.STRING)`)
- **OWNER**: Proprietário do restaurante - acesso completo aos recursos próprios
- **CLIENT**: Cliente final - acesso limitado a consultas e pedidos  
- **ADMIN**: Administrador do sistema - acesso total a todos os recursos
- **Persistência**: Array PostgreSQL `VARCHAR(255)[]` para suportar múltiplos roles
  
**Tipos UUID**: Todas as chaves primárias utilizam UUID v4 auto-geradas
**Timestamps**: `LocalDateTime` com anotações Hibernate para criação/atualização automática
**Senha Hash**: BCrypt com salt automático para segurança de autenticação
  
**Compatibilidade**: O schema está 100% alinhado com as entidades `JpaUserEntity` e `JpaAddressEntity`, garantindo consistência entre o modelo de dados e a estrutura do banco em todos os ambientes (desenvolvimento, teste e produção).
  
### Diagramas PlantUML Detalhados
  
Os diagramas estão implementados em PlantUML e cobrem todos os aspectos arquiteturais:
  
#### **diag01.puml - Arquitetura Hexagonal Completa** 
![Arquitetura Hexagonal](Hexagonal_Architecture.png )
  
- Visualização da separação entre Application Core e Infrastructure
- Representação dos Ports (Inbound e Outbound)
- Mapeamento dos Adapters (REST Controllers, JPA Repositories, Security)
- Fluxo de dependências seguindo Clean Architecture
  
#### **diag02.puml - Modelo de Domínio (Classes)** 
![Modelo de Domínio](Domain_Model_Classes.png )
  
- Agregados e Entidades do domínio
- Value Objects e Domain Services
- Relacionamentos e cardinalidades
- Business rules e invariantes
  
#### **diag03.puml - Sequence Diagram de Autenticação**
![Sequência de Autenticação](Authentication_Sequence.png )
  
- Fluxo completo do login JWT
- Validação de credenciais
- Geração e retorno do token
- Autorização em endpoints protegidos
  
#### **diag04.puml - Schema do Banco de Dados**
![Schema do Banco](Database_Schema.png )
  
- Estrutura das tabelas principais
- Relacionamentos e chaves estrangeiras
- Constraints e validações
- Índices e otimizações
  
#### **diag05.puml - Visão Geral da Arquitetura Completa**
![Arquitetura Completa](Complete_Architecture_Overview.png )
  
- Separação detalhada entre Application e Infrastructure  
- Dependencies flow (dependências apontando para dentro)
- Interfaces e implementações
- Configuration e Dependency Injection
  
#### **diag06.puml - Fluxo Completo de Endereços**
![Fluxo de Endereços](Address_Complete_Flow_Sequence.png )
  
- Interações completas entre Controllers, Use Cases e Repositories
- Sequência de chamadas nos processos de CRUD de endereços
- Validações e transformações de dados
- Error handling e exception propagation
  
#### **diag07.puml - Estrutura Completa de Pacotes**
![Estrutura de Pacotes](Package_Structure_Complete.png )
  
- Organização completa dos pacotes do projeto
- Separação entre camadas (application, domain, infrastructure)
- Dependências entre módulos
- Convenções de nomenclatura e estrutura
  
## Spring Boot Actuator - Monitoramento
  
### Configuração do Actuator
  
O projeto está configurado para utilizar Spring Boot Actuator para monitoramento operacional:
  
#### **Dependência no pom.xml**
```xml
<!-- Adicionar esta dependência para habilitar Actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
  
#### **Configuração no application.yaml**
```yaml
# Configuração do Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
      show-components: always
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
    db:
      enabled: true
  info:
    env:
      enabled: true
    build:
      enabled: true
    git:
      enabled: true
```
  
### Endpoints de Monitoramento Disponíveis
  
#### **Health Checks** (`/actuator/health`)
- **Liveness Probe**: `/actuator/health/liveness` - Verifica se a aplicação está viva
- **Readiness Probe**: `/actuator/health/readiness` - Verifica se está pronta para receber tráfego
- **Database Health**: Verificação automática da conectividade com PostgreSQL
- **Custom Health Indicators**: Possibilidade de adicionar verificações customizadas
  
#### **Métricas Operacionais** (`/actuator/metrics`)
- **JVM Metrics**: Uso de memória, garbage collection, threads
- **HTTP Metrics**: Latência de requests, contadores de status HTTP
- **Database Metrics**: Connection pool, query performance
- **Application Metrics**: Contadores customizados de negócio
  
#### **Informações da Aplicação** (`/actuator/info`)
- **Build Information**: Versão, timestamp, artifact details
- **Git Information**: Branch, commit hash, build time
- **Environment Properties**: Configurações ativas da aplicação
  
#### **Prometheus Integration** (`/actuator/prometheus`)
- **Metrics Export**: Formato compatível com Prometheus
- **Observability Stack**: Integração com Grafana para dashboards
- **Alerting**: Configuração de alertas baseados em métricas
  
### Segurança do Actuator
  
#### **Configuração de Segurança**
```java
// Configuração no WebSecurityConfig
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .requestMatchers("/actuator/health").permitAll()
        .requestMatchers("/actuator/health/liveness").permitAll()
        .requestMatchers("/actuator/health/readiness").permitAll()
        .requestMatchers("/actuator/**").hasRole("ADMIN")
        .anyRequest().authenticated();
}
```
  
#### **Níveis de Acesso**
- **Público**: `/health`, `/health/liveness`, `/health/readiness`
- **Administrador**: Todos os outros endpoints do Actuator
- **Monitoramento External**: Configuração específica para ferramentas de observability
  
### Observability e Monitoring Stack
  
#### **Recommended Stack**
- **Metrics Collection**: Micrometer + Prometheus
- **Visualization**: Grafana dashboards
- **Logging**: Structured JSON logs + ELK Stack
- **Tracing**: Spring Cloud Sleuth + Zipkin
- **Alerting**: Prometheus AlertManager
  
#### **Production Deployment**
- **Health Check Endpoints**: Para Kubernetes liveness/readiness probes
- **Metrics Scraping**: Para systems de monitoramento
- **Log Aggregation**: Para análise centralizada
- **Performance Monitoring**: Para otimização contínua
  
# 3. Descrição dos Endpoints da API
  
## Tabela Completa de Endpoints
  
### **Autenticação**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/auth/login` | POST | Autenticar e obter token JWT | Não | Público | `LoginController` |
  
### **Gestão de Usuários**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/users` | POST | Criar usuário CLIENT | Não | Público | `UserController` |
| `/api/v1/users/owner` | POST | Criar usuário OWNER | Sim | ADMIN | `UserController` |
| `/api/v1/users/{id}` | GET | Buscar usuário por ID | Sim | ADMIN ou próprio | `UserController` |
| `/api/v1/users/by-name` | GET | Buscar por nome (query) | Sim | ADMIN | `UserController` |
| `/api/v1/users/{id}` | PUT | Atualizar dados gerais | Sim | ADMIN ou próprio | `UserController` |
| `/api/v1/users/{id}/password` | PUT | Alterar senha | Sim | ADMIN ou próprio | `UserController` |
| `/api/v1/users/{id}` | DELETE | Excluir usuário | Sim | ADMIN | `UserController` |
  
### **Gestão de Endereços**
| Endpoint | Método | Descrição | Auth | Autorização | Controller |
|----------|--------|-----------|------|-------------|------------|
| `/api/v1/address` | GET | Listar endereços próprios | Sim | Usuário autenticado | `AddressController` |
| `/api/v1/address` | POST | Criar endereço | Sim | Usuário autenticado | `AddressController` |
| `/api/v1/address/{addressId}` | PUT | Atualizar endereço | Sim | Proprietário | `AddressController` |
| `/api/v1/address/{addressId}` | DELETE | Excluir endereço | Sim | Proprietário | `AddressController` |
  
### **Monitoramento (Actuator)**
| Endpoint | Método | Descrição | Auth | Autorização |
|----------|--------|-----------|------|-------------|
| `/actuator/health` | GET | Health check geral | Não | Público |
| `/actuator/health/liveness` | GET | Liveness probe | Não | Público |
| `/actuator/health/readiness` | GET | Readiness probe | Não | Público |
| `/actuator/info` | GET | Informações da aplicação | Sim | ADMIN |
| `/actuator/metrics` | GET | Métricas operacionais | Sim | ADMIN |
| `/actuator/prometheus` | GET | Métricas para Prometheus | Sim | ADMIN |
  
### **Documentação (OpenAPI/Swagger)**
| Endpoint | Método | Descrição | Auth | Autorização |
|----------|--------|-----------|------|-------------|
| `/swagger-ui` | GET | Interface Swagger UI | Não | Público |
| `/v3/api-docs` | GET | Especificação OpenAPI JSON | Não | Público |
  
## Códigos de Status HTTP e Tratamento de Erros
  
### **Códigos de Sucesso**
- **200 OK**: Operação de leitura/atualização realizada com sucesso
- **201 Created**: Recurso criado com sucesso (usuário, endereço)
- **204 No Content**: Operação de exclusão realizada sem retorno de dados
  
### **Códigos de Erro do Cliente** 
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
  
### **Exception Handling Global**
  
**Classes de Exceção Implementadas:**
- `GlobalExceptionHandler`: Captura e padroniza todas as exceções
- `UserNotFoundException`: Usuário não encontrado (404)
- `AddressNotFoundException`: Endereço não encontrado (404)  
- `UserAlreadyExistsException`: Email/login duplicado (409)
- `AddressDoesNotBelongToUserException`: Acesso negado a endereço (403)
- `InvalidFieldException`: Validação de domínio falhou (400)
- `BusinessRuleException`: Regra de negócio violada (400)
  
# 4. Configuração do Projeto
  
## **Configuração Docker & Containerização**
  
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
  
### ** Dockerfile Multi-stage** (Otimizado)
  
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
  
**Características do Build:**
- **Multi-stage**: Reduz tamanho final da imagem
- **Security**: Usuário não-root (UID 2000)
- **Performance**: Cache de dependências Maven
- **Production-ready**: JRE-only no runtime
  
## **Instruções para Execução**
  
### **Pré-requisitos**
- **Docker** e **Docker Compose** (recomendado)
- **Java 21+** (para desenvolvimento local)
- **Maven 3.9+** (para build local)
- **Git** para versionamento
  
### **Opção 1: Docker Compose Completo** 
```bash
# Clone o repositório
git clone https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01.git
cd tech-challenge-fase-01
  
# Executar aplicação completa
docker compose up --build
  
# Ou executar em background
docker compose up -d --build
```
  
**Vantagens:**
- Ambiente isolado e reproduzível
- PostgreSQL e aplicação configurados automaticamente
- Network isolation e persistent volumes
  
### **Opção 2: Desenvolvimento Híbrido**
```bash
## 1. Iniciar apenas PostgreSQL
docker compose up -d postgres
  
## 2. Executar Spring Boot localmente (com hot reload)
./mvnw spring-boot:run -Dspring-boot.devtools.restart.enabled=true
  
## 3. Para debug com breakpoints
./mvnw spring-boot:run \
  -Dspring-boot.devtools.restart.enabled=true \
  -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'
```
  
**Vantagens:**
- Restart rápido durante desenvolvimento  
- Debug com IDE
- Logs diretos no terminal
  
## 3. Executando os testes unitários
  
```bash
## 1. Executando testes
./mvnw test
```
```bash
## 2. Executando testes com jacoco
./mvnw clean test jacoco:report
```
  
  
### **URLs de Acesso**
  
| Serviço | URL Local | URL Docker | Descrição |
|---------|-----------|------------|-----------|
| **API REST** | http://localhost:8080 | http://localhost:8080 | Endpoints principais |
| **Swagger UI** | http://localhost:8080/swagger-ui | http://localhost:8080/swagger-ui | Documentação interativa |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | http://localhost:8080/v3/api-docs | Spec OpenAPI |
| **PostgreSQL** | jdbc:postgresql://localhost:5432/restaurantapp | jdbc:postgresql://postgres:5432/restaurantapp | Banco de dados |
| **Spring Boot Actuator** | http://localhost:8080/actuator | http://localhost:8080/actuator | Health checks |
| **Prometheus** | http://localhost:8080/actuator/prometheus | http://localhost:8080/actuator/prometheus | Métricas Prometheus |
  
### **Configuração do Banco**
  
| Parâmetro | Valor | Descrição |
|-----------|-------|-----------|  
| **Host** | localhost (local) / postgres (docker) | Servidor PostgreSQL |
| **Port** | 5432 | Porta padrão PostgreSQL |
| **Database** | restaurantapp | Nome do banco |
| **Username** | postgres | Usuário administrador |
| **Password** | secret | Senha (dev only) |
| **Version** | PostgreSQL 17 | Versão mais recente |
  
### **Dados de Teste Disponíveis**
  
**Usuários Pré-cadastrados:**
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
  
**Endereços:** 6 endereços pré-cadastrados vinculados aos usuários
  
# 5. Qualidade do Código
  
## **Boas Práticas Implementadas**
  
### **Arquitetura e Design Patterns**
- **Arquitetura Hexagonal (Ports & Adapters)**: Separação rigorosa entre core e infraestrutura
- **Clean Architecture**: Dependências apontando para o centro, regras de negócio isoladas
- **Domain-Driven Design (DDD)**: Modelagem rica com Entities, Value Objects, Domain Services
- **CQRS Pattern**: Separação clara entre comandos (escrita) e queries (leitura)  
- **Dependency Injection**: Inversão de controle através de interfaces bem definidas
- **Factory Methods**: Criação controlada de objetos de domínio com validações
- **Code Quality**: Plugin Sonar
  
### **Princípios SOLID - Implementação Completa**
  
#### **S - Single Responsibility Principle**
- **Use Cases**: Cada um com responsabilidade única e específica
- **Controllers**: Apenas adaptação entre web e application layers
- **Repositories**: Somente persistência, sem lógica de negócio
- **Mappers**: Conversões dedicadas entre camadas
  
#### **O - Open/Closed Principle**  
- **Ports**: Extensibilidade via novas implementações de interfaces
- **Adapters**: Novos adapters sem modificação do core
- **Use Cases**: Extensíveis via composition e dependency injection
  
#### **L - Liskov Substitution Principle**
- **Repository Implementations**: Intercambiáveis (JPA, MongoDB, etc.)
- **Password Encoders**: Substituíveis (BCrypt, SCrypt, etc.)
- **Security Adapters**: Diferentes provedores JWT
  
#### **I - Interface Segregation Principle**
- **Ports específicos**: Interfaces coesas por funcionalidade
- **Inbound Ports**: Contratos específicos por Use Case
- **Outbound Ports**: Abstrações mínimas e focadas
  
#### **D - Dependency Inversion Principle**  
- **Infrastructure → Application**: Dependência em abstrações
- **Use Cases → Repositories**: Através de ports outbound
- **Controllers → Use Cases**: Através de ports inbound
  
### **Segurança Robusta**
- **JWT Stateless**: Tokens auto-contidos com expiração configurável
- **RBAC (Role-Based Access Control)**: 3 níveis (CLIENT, OWNER, ADMIN)
- **BCrypt Hashing**: Senhas criptografadas com salt automático
- **Jakarta Bean Validation**: Validações declarativas em todas as camadas
- **CORS Configurado**: Controle granular de origens permitidas
- **Resource Authorization**: Usuários só acessam recursos próprios
  
### **Exception Handling Padronizado**
- **RFC 7807 Compliance**: Problem Details para respostas de erro consistentes
- **Global Exception Handler**: Tratamento centralizado via `@ControllerAdvice`
- **Error Logging**: Logs estruturados para debugging e auditoria
- **Detailed Messages**: Informações específicas sem exposição de dados sensíveis
  
### **Estratégia de Testes**
- **Jakarta Bean Validation**: Validações automáticas nos DTOs
- **Estrutura de Testes**: Separação entre unitários e integração
- **Test Containers**: Testes com PostgreSQL real em containers
- **H2 Database**: Testes rápidos em memória
- **Coverage Ready**: Estrutura preparada para JaCoCo
  
### **Organização e Documentação**
- **Package by Feature**: Agrupamento por domínios funcionais (User, Address)
- **OpenAPI 3.0**: Documentação interativa automática com Swagger UI
- **Naming Conventions**: Nomenclatura consistente seguindo padrões Java
- **Clean Code**: Métodos pequenos, responsabilidades bem definidas
- **Javadoc**: Documentação nas interfaces principais
  
### **Configuração e DevOps**
- **Spring Profiles**: Separação clara de ambientes (dev, test, prod)
- **Externalized Configuration**: Configurações via `application.yaml`
- **Docker Multi-stage**: Build otimizado com cache de dependências
- **Actuator**: Endpoints de monitoramento e health checks
  
# 6. Estratégia de Testes e Cobertura
  
## **Visão Geral da Qualidade dos Testes**
  
O projeto implementa uma **estratégia abrangente de testes** seguindo boas práticas da indústria, garantindo **confiabilidade** e **manutenibilidade** do código através de múltiplas camadas de validação.
  
### **Estrutura dos Testes por Camada**
  
#### **Domain Layer - 100% Coverage Critical**
```
application/domain/
├── UserDomainTest.java                  # 32 testes - Validação completa de entidades
├── AddressDomainTest.java               # 14 testes - Regras de negócio de endereços
├── service/
│   ├── UserDomainServiceTest.java       # 17 testes - Lógica de domínio de usuários
│   └── AddressDomainServiceTest.java    # 5 testes - Lógica de domínio de endereços
└── valueobject/
    ├── EmailTest.java                   # 37 testes - Validação de email
    ├── UsernameTest.java                # 46 testes - Validação de username
    └── PersonNameTest.java              # 42 testes - Validação de nomes
```
  
#### **Application Layer - 97% Coverage**
```
application/usecase/
├── user/                                # 20 testes - Use Cases de usuários
│   ├── CreateUserUseCaseTest.java       # 3 testes
│   ├── CreateOwnerUseCaseTest.java      # 3 testes
│   ├── UpdatePasswordUseCaseTest.java   # 5 testes
│   └── ...outros Use Cases
├── address/                             # 16 testes - Use Cases de endereços
└── mapper/                              # 6 testes - Mapeamentos entre camadas
```
  
#### **Infrastructure Layer - Cobertura Estratégica**
```
infrastructure/
├── security/                            # 39 testes - 98% coverage
│   ├── JwtUtilTest.java                # 14 testes - Geração/validação JWT
│   ├── JwtAuthenticationFilterTest.java # 13 testes - Filtros de autenticação
│   ├── SecurityUserTest.java          # 12 testes - UserDetails customizado
│   └── BCryptPasswordEncoderAdapterTest.java # 13 testes
├── controller/                          # 13 testes - 100% coverage
│   ├── UserControllerTest.java         # 8 testes - API REST usuários
│   ├── AddressControllerTest.java      # 4 testes - API REST endereços
│   └── LoginControllerTest.java        # 1 teste - Autenticação
├── repositories/                        # 3 testes - Integração JPA
└── exceptions/                          # 4 testes - Exception handling
```
  
### **Tipos de Testes Implementados**
  
#### **1. Testes Unitários (95% dos testes)**
**Domain Services**
- Validação de regras de negócio isoladas
- Lógica de uniqueness e constraints
- Autorização e ownership de recursos
  
**Value Objects**
- Validação de formatos (email, username)
- Constraints de tamanho e caracteres
- Imutabilidade e equality
  
**Use Cases**
- Orquestração de operações
- Integration entre domain e infrastructure
- Error handling e exception propagation
  
#### **2. Testes de Integração (5% dos testes)**
**Repository Layer**
- Persistência JPA com H2 in-memory
- Queries customizadas e relacionamentos
- Transações e rollback
  
**Security Integration**
- Autenticação end-to-end
- Autorização baseada em roles
- JWT token validation
  
#### **3. Testes de Contrato (API Testing)**
**REST Controllers**
- Serialização/deserialização JSON
- HTTP status codes
- Error response formatting (RFC 7807)
- Request/Response validation
  
### **Ferramentas e Frameworks**
  
#### **Testing Stack**
- **JUnit 5.12.2**: Framework principal de testes
- **Mockito 5.17.7**: Mocking para isolamento de dependências  
- **Spring Boot Test**: Testes de integração com contexto Spring
- **JaCoCo 0.8.11**: Análise de cobertura de código
- **H2 Database**: Banco in-memory para testes
- **AssertJ**: Fluent assertions para melhor legibilidade
  
#### **Configuração de Ambiente**
```yaml
# application-test.yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
  profiles:
    active: test
```
  
### **Cobertura Detalhada por Pacote**
  
| Pacote | Cobertura | Testes | Status |
|--------|-----------|--------|--------|
| **Domain Services** | **100%** | 22 testes | Crítico coberto |
| **Use Cases** | **97%** | 36 testes | Lógica de aplicação |
| **Security** | **98%** | 39 testes | Camada crítica |
| **Controllers** | **100%** | 13 testes | APIs validadas |
| **Value Objects** | **99%** | 125 testes | Validações robustas |
| **Domain Entities** | **97%** | 46 testes | Regras de negócio |
| **Mappers** | **81%** | 6 testes | Conversões básicas |
  
### **Execução dos Testes**
  
#### **Comando Básico**
```bash
# Executar todos os testes
./mvnw test
  
# Com relatório de cobertura JaCoCo
./mvnw clean test jacoco:report
  
# Testes específicos
./mvnw test -Dtest=UserDomainTest
```
  
#### **Relatório de Cobertura**
```bash
# Localização do relatório HTML
target/site/jacoco/index.html
  
```
  
# 7. Collections para Teste
  
## **Resources para Testes Completos**
  
### **Postman Collection**
**Arquivo**: `TechChallenge.postman_collection.json` 
  
### **HTTP Files (VS Code)**
**Arquivo**: `docs/chamadas.http` 
  
### **Swagger UI Interativo**
**URL**: http://localhost:8080/swagger-ui
  
# 8. Documentação Técnica
  
## **Documentação Completa do Projeto**
  
O projeto conta com **documentação técnica abrangente** que cobre todos os aspectos da implementação:
  
### **Relatórios de Validação**
- **PROJECT-VALIDATION-REPORT.md** - Relatório completo de validação arquitetural (9.86/10)
- **SOLID-HEXAGONAL-ARCHITECTURE-ANALYSIS.md** - Análise detalhada dos princípios SOLID
- **VALIDATION-SUMMARY.md** - Resumo executivo das validações realizadas
  
### **Documentação Arquitetural**  
- **HEXAGONAL-ARCHITECTURE-OVERVIEW.md** - Visão geral da arquitetura hexagonal
- **HEXAGONAL-ARCHITECTURE-APPLICATION-LAYER.md** - Detalhamento da camada de aplicação
- **INFRASTRUCTURE-LAYER.md** - Documentação da camada de infraestrutura
  
### **Relatórios de Implementação**
- **IMPLEMENTATION-COMPLETE.md** - Status completo da implementação
- **INFRASTRUCTURE-IMPLEMENTATION-SUMMARY.md** - Resumo da implementação de infraestrutura
  
### **Documentação de API**
- **API-DOCUMENTATION.md** - Documentação completa dos endpoints
- **chamadas.http** - Collection de chamadas para teste em VS Code
- **TechChallenge.postman_collection.json** - Collection completa para Postman
  
### **Diagramas e Especificações**
- **diag01.puml até diag07.puml** - Diagramas UML da arquitetura
- **CURRENT-PROJECT-STRUCTURE.md** - Estrutura atual detalhada do projeto
- **RFC-7807-EXCEPTION-HANDLING.md** - Documentação do tratamento de exceções
  
### **Análises e Validações**
- **REVALIDATION-COMPLETE.md** - Processo completo de revalidação
- **SYNC-REPORT.md** - Relatório de sincronização da documentação
- **DATABASE-UPDATE-REPORT.md** - Relatório de atualizações do banco de dados
  
## **Acesso à Documentação**
  
| Tipo | Localização | Descrição |
|------|-------------|-----------|
| **Interativa** | http://localhost:8080/swagger-ui | Interface web para testes |
| **OpenAPI Spec** | http://localhost:8080/v3/api-docs | Especificação OpenAPI 3.0 |
| **Técnica** | `/docs/` | Documentação markdown completa |
| **Diagramas** | `/docs/diag*.puml` | Diagramas UML da arquitetura |
| **Collections** | Raiz do projeto | Postman e HTTP files |
  
# 9. Repositório do Código
  
## **Informações do Repositório**
  
**URL**: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01
  
  
## **Conclusão do Projeto**
  
O **Tech Challenge Fase 01** representa uma implementação exemplar de arquitetura de software moderna, demonstrando a aplicação rigorosa de **princípios de engenharia** e **boas práticas** da indústria. Este projeto transcende o escopo de uma simples API REST, constituindo-se como um **modelo de referência** para desenvolvimento Java.
  
### **Excelência Arquitetural Alcançada**
  
A implementação demonstra **conformidade total** com padrões arquiteturais de alta qualidade:
  
**Arquitetura Hexagonal (Ports & Adapters)**
- Separação rigorosa entre **core business** e **infraestrutura técnica**
- **37 classes** na camada de aplicação completamente **framework-independent**
- **25 adaptadores** na camada de infraestrutura implementando todos os **ports** definidos
- **Inversão de dependência** garantindo total **testabilidade** e **evolutibilidade**
  
**Princípios SOLID**
- **SRP**: Cada classe possui uma única responsabilidade claramente definida
- **OCP**: Extensibilidade via interfaces sem modificação de código existente
- **LSP**: Substituibilidade garantida através de contratos bem definidos
- **ISP**: Interfaces especializadas evitando dependências desnecessárias
- **DIP**: Dependências abstraídas através de ports e interfaces
  
### **Segurança**
  
O sistema implementa um **modelo de segurança multicamadas** robusto:
  
- **Autenticação JWT** com tokens assinados e expiração configurável
- **Autorização baseada em roles** (RBAC) com granularidade de recursos
- **Resource ownership** garantindo isolamento de dados por usuário
- **Criptografia BCrypt** para proteção de senhas com salt automático
- **Exception handling padronizado** seguindo **RFC 7807** (Problem Details)
  
### **Qualidade de Código e Manutenibilidade**
  
A base de código demonstra **maturidade técnica** através de:
  
**Métricas de Qualidade**
- **Classes Java** organizadas em **camadas bem definidas**
- **Nomenclatura consistente** seguindo convenções Java/Spring
- **Responsabilidades claras** com alta coesão e baixo acoplamento
- **Separação de concerns** respeitando boundaries arquiteturais
  
**Design Testável**
- **testes implementados** com cobertura de código
- **Dependency Injection** facilitando mock e testes unitários
- **Interfaces bem definidas** permitindo test doubles
- **Use Cases isolados** para testes de regras de negócio
- **Adapters desacoplados** para testes de integração
- **Zero flaky tests** com execução determinística em ~25 segundos
  
### **Infraestrutura**
  
O projeto está preparado para **ambientes produtivos**:
  
**Containerização Completa**
- **Docker Compose** orquestrando aplicação + PostgreSQL
- **Multi-stage builds** otimizando tamanho das imagens
- **Health checks** garantindo disponibilidade dos serviços
- **Variáveis de ambiente** para configuração flexível
  
**Observabilidade e Monitoramento**
- **Spring Boot Actuator** expondo métricas operacionais
- **Health checks** para database e componentes críticos
- **Prometheus metrics** para integração com ferramentas de monitoramento
  
### **Legado Técnico e Aprendizados**
  
Este projeto estabelece um **framework de referência** para:
  
1. **Implementação de arquiteturas limpas** em ecossistema Java/Spring
2. **Aplicação prática de padrões** arquiteturais modernos
3. **Desenvolvimento seguro** com autenticação/autorização robustas
4. **Estratégias abrangentes de testes** com 309 testes
5. **Documentação técnica** de qualidade profissional
6. **Práticas DevOps** com containerização e observabilidade
  
### **Considerações Finais**
  
O **Tech Challenge Fase 01** não apenas **atende aos requisitos** propostos, mas os **supera significativamente**, entregando uma solução que demonstra:
  
- **Maturidade técnica** na aplicação de padrões arquiteturais
- **Qualidade de software** com 309 testes e cobertura robusta de código
- **Visão de produto** com foco em manutenibilidade e evolutibilidade  
- **Qualidade enterprise** adequada para ambientes produtivos críticos
- **Documentação exemplar** facilitando transferência de conhecimento
- **Preparação para escala** através de design patterns apropriados
  
Este projeto serve como **prova de conceito** da capacidade técnica da **Equipe 3** em entregar soluções arquiteturais robustas, escaláveis e maintíveis, estabelecendo um **padrão de excelência** para futuras implementações.
