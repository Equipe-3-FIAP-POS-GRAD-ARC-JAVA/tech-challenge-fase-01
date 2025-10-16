# Infrastructure Layer - Hexagonal Architecture Implementation

## Overview

A camada de infraestrutura implementa os **Adapters** da Arquitetura Hexagonal, conectando a camada de aplicação (core) com o mundo externo. Esta camada segue rigorosamente os princípios SOLID e as melhores práticas de Clean Architecture.

## Estrutura da Camada

```
infrastructure/
├── adapters/
│   ├── inbound/           # Driving Adapters (quem chama a aplicação)
│   │   ├── web/
│   │   │   ├── rest/
│   │   │   │   ├── controller/      # Controllers REST
│   │   │   │   ├── dto/             # DTOs da camada web
│   │   │   │   └── mapper/          # Mappers web ↔ application
│   │   │   └── handler/             # Exception handlers
│   │   └── security/                # Segurança Spring Security
│   │       ├── jwt/                 # Implementação JWT
│   │       └── dto/                 # DTOs de segurança
│   └── outbound/          # Driven Adapters (chamados pela aplicação)
│       ├── repositories/            # Implementação de persistência
│       ├── security/                # Adapters de segurança
│       ├── entities/                # Entidades JPA
│       └── mappers/                 # Mappers domain ↔ entity
└── configs/               # Configurações Spring
    ├── SecurityBeansConfig.java    # Beans de use cases
    └── WebSecurityConfig.java      # Configuração de segurança
```

## Componentes Principais

### 1. Controllers (Adapters Inbound)

#### UserController
- **Responsabilidade**: Gerenciar requisições HTTP relacionadas a usuários
- **Princípios SOLID**:
  - **SRP**: Apenas converte DTOs e delega para use cases
  - **OCP**: Extensível via novos endpoints sem modificar existentes
  - **DIP**: Depende de abstrações (ports), não de implementações
- **Endpoints**:
  - `POST /api/v1/users` - Criar usuário CLIENT (público)
  - `POST /api/v1/users/owner` - Criar usuário OWNER (apenas ADMIN)
  - `GET /api/v1/users/{id}` - Buscar por ID
  - `GET /api/v1/users/by-name` - Buscar por nome
  - `PUT /api/v1/users/{id}` - Atualizar usuário
  - `PATCH /api/v1/users/{id}/password` - Atualizar senha
  - `DELETE /api/v1/users/{id}` - Deletar usuário (apenas ADMIN)

#### LoginController
- **Responsabilidade**: Gerenciar autenticação via JWT
- **Endpoint**:
  - `POST /login` - Autenticar e obter token JWT (público)

### 2. Security (JWT)

#### JwtTokenManager
- **Responsabilidade**: Gerenciar geração e validação de tokens JWT
- **Funcionalidades**:
  - Gerar token com username e roles
  - Validar token
  - Extrair informações do token
- **Configuração**: Usa propriedades `jwt.*` do application.properties

#### JwtAuthenticationFilter
- **Responsabilidade**: Interceptar requisições e validar tokens JWT
- **Fluxo**:
  1. Extrai token do header `Authorization: Bearer <token>`
  2. Valida token
  3. Carrega usuário via UserDetailsService
  4. Configura SecurityContext do Spring Security

#### SecurityUserDetailsService
- **Responsabilidade**: Carregar usuário para autenticação do Spring Security
- **Adaptação**: Converte `UserDomain` → `UserDetails`

### 3. Adapters Outbound

#### UserRepositoryImpl
- **Responsabilidade**: Implementar `UserRepositoryPort` usando JPA
- **Princípios**:
  - **SRP**: Apenas persistência, sem lógica de negócio
  - **DIP**: Implementa port da aplicação
- **Adaptação**: Converte `UserDomain` ↔ `JpaUserEntity`

#### JwtTokenAdapter
- **Responsabilidade**: Adaptar `JwtTokenManager` para `JwtTokenPort`
- **Vantagem**: Camada de aplicação não conhece detalhes de JWT

#### BCryptPasswordEncoderAdapter
- **Responsabilidade**: Adaptar `PasswordEncoder` do Spring para `PasswordEncoderPort`
- **Vantagem**: Permite trocar algoritmo de hash sem afetar aplicação

### 4. Mappers

#### UserWebMapper
- **Responsabilidade**: Converter DTOs entre camadas web e aplicação
- **Conversões**:
  - `UserCreateRequestDTO` → `UserCreateRequest`
  - `UserUpdateRequestDTO` → `UserUpdateRequest`
  - `UpdatePasswordRequestDTO` → `UpdatePasswordRequest`
  - `UserResponse` → `UserResponseDTO`

#### AuthWebMapper
- **Responsabilidade**: Converter DTOs de autenticação
- **Conversões**:
  - `LoginRequest` (web) → `LoginRequest` (application)
  - `LoginResponse` (application) → `LoginResponse` (web)

## Configuração

### SecurityBeansConfig
Configura todos os beans da camada de aplicação:
- **Use Cases**: Cria instâncias de todos os use cases
- **Services**: UserDomainService
- **Security**: PasswordEncoder, AuthenticationManager, AuthenticationProvider

### WebSecurityConfig
Configura segurança Spring Security:
- **JWT**: Filtro de autenticação JWT
- **Endpoints públicos**: `/login`, `/api/v1/users` (POST), `/api/health`
- **Endpoints protegidos**: Todos os outros requerem autenticação
- **CORS**: Habilitado
- **CSRF**: Desabilitado (API REST stateless)
- **Session**: STATELESS (não usa sessões)

### Application Properties
```properties
# JWT Configuration
jwt.issuer=tech-challenge-fase-01
jwt.secret-key=${JWT_SECRET_KEY:change-in-production}
jwt.expiration-minute=${JWT_EXPIRATION_MINUTE:60}
```

## Segurança JWT

### Fluxo de Autenticação
1. Cliente envia `POST /login` com credenciais
2. `LoginController` recebe requisição
3. `AuthWebMapper` converte DTO
4. `AuthUseCases` valida credenciais
5. `JwtTokenAdapter` gera token
6. Token retornado ao cliente

### Fluxo de Autorização
1. Cliente envia requisição com header `Authorization: Bearer <token>`
2. `JwtAuthenticationFilter` intercepta
3. Token extraído e validado
4. `SecurityUserDetailsService` carrega usuário
5. `SecurityContext` configurado com usuário autenticado
6. Request prossegue para controller

### Roles e Permissões
- **CLIENT**: Usuário comum, pode gerenciar próprios dados
- **OWNER**: Dono de restaurante, permissões estendidas
- **ADMIN**: Administrador, acesso total

## Princípios SOLID Aplicados

### Single Responsibility Principle (SRP)
- Cada controller gerencia apenas um recurso REST
- Cada mapper converte apenas entre duas camadas específicas
- Cada adapter tem uma única responsabilidade de adaptação

### Open/Closed Principle (OCP)
- Novos endpoints podem ser adicionados sem modificar existentes
- Novos adapters podem ser criados sem modificar a aplicação
- Configuração extensível via Spring beans

### Liskov Substitution Principle (LSP)
- Todos os adapters implementam seus respectivos ports
- Podem ser substituídos por outras implementações
- Testes podem usar mocks que implementam os mesmos ports

### Interface Segregation Principle (ISP)
- DTOs separados por camada (web vs application)
- Ports específicos para cada operação
- Controllers dependem apenas dos ports necessários

### Dependency Inversion Principle (DIP)
- Controllers dependem de ports (abstrações), não de use cases concretos
- Adapters implementam ports da aplicação
- Spring injeta implementações via configuration

## Arquitetura Hexagonal Aplicada

### Adapters Inbound (Driving)
Adaptadores que **chamam** a aplicação:
- **Controllers REST**: Recebem requisições HTTP
- **Security Filters**: Interceptam e validam requisições

### Adapters Outbound (Driven)
Adaptadores **chamados** pela aplicação:
- **Repository Adapters**: Persistência de dados
- **Security Adapters**: Criptografia, JWT
- **External Services**: APIs externas (futuro)

### Ports
Interfaces que definem contratos:
- **Inbound Ports**: Use cases que controllers podem chamar
- **Outbound Ports**: Repositórios e serviços que use cases precisam

## Validações

### Validações de Entrada (DTOs)
- `@NotBlank`: Campos obrigatórios
- `@Email`: Formato de email
- `@Size`: Tamanho mínimo/máximo
- `@Valid`: Validação em cascade

### Validações de Negócio
- Feitas na camada de aplicação (use cases e domain)
- Infraestrutura apenas valida formato/presença

## Exception Handling

### GlobalExceptionHandler
- Captura exceções da aplicação
- Converte para respostas HTTP apropriadas
- Segue RFC 7807 (Problem Details)

### Mapeamento de Exceções
- `UserNotFoundException` → 404 NOT FOUND
- `UserAlreadyExistsException` → 409 CONFLICT
- `DomainValidationException` → 400 BAD REQUEST
- `UnauthorizedException` → 401 UNAUTHORIZED

## Testes

### Controllers
- Testes de integração com `@WebMvcTest`
- Mocking de use cases
- Validação de status codes e JSON

### Mappers
- Testes unitários
- Verificação de conversões corretas
- Handling de nulls

### Security
- Testes de autenticação/autorização
- Validação de tokens
- Testes de endpoints públicos/protegidos

## Melhorias Futuras

1. **Rate Limiting**: Proteção contra abuso de API
2. **API Versioning**: Suporte a múltiplas versões
3. **OpenAPI/Swagger**: Documentação automática
4. **CORS Configurável**: Configuração mais granular
5. **Audit Logging**: Log de todas as operações
6. **Metrics**: Prometheus/Micrometer para métricas
7. **Health Checks**: Endpoints de saúde mais detalhados

## Referências

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT (RFC 7519)](https://datatracker.ietf.org/doc/html/rfc7519)
